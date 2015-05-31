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
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

import org.arakhne.afc.sizediterator.ArraySizedIterator;
import org.arakhne.afc.sizediterator.SizedIterator;

/**
 * Some utilities functions for arrays. 
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ArrayUtil {

	/** Reverse the specified array.
	 * 
	 * @param <T>
	 * @param tab
	 */
	public static <T> void reverse(T[] tab) {
		T tmp;
		for(int i=0; i<tab.length/2; ++i) {
			tmp = tab[i];
			tab[i] = tab[tab.length-i-1];
			tab[tab.length-i-1] = tmp;
		}
	}

	/** Replies an array that corresponds to the given collection.
	 * 
	 * @param <T> is the type of the elements.
	 * @param collection is the collection to translate
	 * @param clazz is the type of the elements.
	 * @return the array.
	 */
	public static <T> T[] toArray(Collection<? extends T> collection, Class<T> clazz) {
		int size = (collection==null) ? 0 : collection.size();
		T[] tab = newInstance(clazz, size);
		if ((collection!=null)&&(size>0)) collection.toArray(tab);
		return tab;
	}

	/** Replies an array that corresponds to the given collection.
	 * <p>
	 * This function clear the content of the given collection.
	 * 
	 * @param <T> is the type of the elements.
	 * @param collection is the collection to translate
	 * @param clazz is the type of the elements.
	 * @return the array.
	 */
	public static <T> T[] toArrayAndClear(Collection<? extends T> collection, Class<T> clazz) {
		T[] tab = toArray(collection,clazz);
		collection.clear();
		return tab;
	}

	/** Merge the arrays.
	 * <p>
	 * This function does not remove the <code>null</code> values.
	 * 
	 * @param <T> is the type of the elements.
	 * @param clazz is the type of the elements.
	 * @param arrays are the arrays to merge.
	 * @return the array.
	 */
	public static <T> T[] merge(Class<T> clazz, @SuppressWarnings("unchecked") T[]... arrays) {
		int length = 0;
		for (T[] tab : arrays) {
			if (tab!=null)
				length += tab.length; 
		}
		T[] result = newInstance(clazz,length);
		int i=0;
		for (T[] tab : arrays) {
			if (tab!=null) {
				System.arraycopy(tab,0,result,i,tab.length);
				i += tab.length;
			}
		}
		return result;
	}
	
	/** Merge the arrays.
	 * <p>
	 * This function removes the <code>null</code> values.
	 * 
	 * @param <T> is the type of the elements.
	 * @param clazz is the type of the elements.
	 * @param arrays are the arrays to merge.
	 * @return the array.
	 */
	public static <T> T[] merge_without_null(Class<T> clazz, @SuppressWarnings("unchecked") T[]... arrays) {
		int length = 0;
		for (T[] tab : arrays) {
			if (tab!=null) {
				for (T t : tab) {
					if (t!=null) ++length;
				}
			}
		}
		T[] result = newInstance(clazz,length);
		int i=0;
		for (T[] tab : arrays) {
			if (tab!=null) {
				for (T t : tab) {
					if (t!=null) {
						result[i] = t;
						++i;
					}
				}
			}
		}
		return result;
	}

	/** Merge the elements to make an array.
	 * <p>
	 * This function does not remove the <code>null</code> values.
	 * 
	 * @param <T> is the type of the elements.
	 * @param clazz is the type of the elements.
	 * @param elements are the elements to merge.
	 * @return the array.
	 */
	public static <T> T[] merge(Class<T> clazz, @SuppressWarnings("unchecked") T... elements) {
		T[] result = newInstance(clazz,elements.length);
		System.arraycopy(elements,0,result,0,elements.length);
		return result;
	}

	/** Merge the elements to make an array.
	 * <p>
	 * This function removes the <code>null</code> values.
	 * 
	 * @param <T> is the type of the elements.
	 * @param clazz is the type of the elements.
	 * @param elements are the elements to merge.
	 * @return the array.
	 */
	public static <T> T[] merge_without_null(Class<T> clazz, @SuppressWarnings("unchecked") T... elements) {
		int l = 0;
		for (T t : elements) {
			if (t!=null) ++l;
		}
		T[] result = newInstance(clazz,l);
		int i=0;
		for (T t : elements) {
			if (t!=null) result[i++] = t;
		}
		return result;
	}

	/** Merge the elements to make an array.
	 * <p>
	 * This function does not remove the <code>null</code> values.
	 * 
	 * @param <T> is the type of the elements.
	 * @param source is the first array to merge.
	 * @param clazz is the type of the elements.
	 * @param elements are the elements to merge.
	 * @return the array.
	 */
	public static <T> T[] merge(Class<T> clazz, T[] source, @SuppressWarnings("unchecked") T... elements) {
		T[] result = newInstance(clazz,source.length+elements.length);
		System.arraycopy(source,0,result,0,source.length);
		System.arraycopy(elements,0,result,source.length,elements.length);
		return result;
	}

	/** Merge the elements to make an array.
	 * <p>
	 * This function removes the <code>null</code> values.
	 * 
	 * @param <T> is the type of the elements.
	 * @param source is the first array to merge.
	 * @param clazz is the type of the elements.
	 * @param elements are the elements to merge.
	 * @return the array.
	 */
	public static <T> T[] merge_without_null(Class<T> clazz, T[] source, @SuppressWarnings("unchecked") T... elements) {
		int l=0;
		for (T t : source) {
			if (t!=null) ++l;
		}
		for (T t : elements) {
			if (t!=null) ++l;
		}
		T[] result = newInstance(clazz,l);
		int i=0;
		for (T t : source) {
			if (t!=null) result[i++] = t;
		}
		for (T t : elements) {
			if (t!=null) result[i++] = t;
		}
		return result;
	}

	/** Remove the given elements from the array.
	 * 
	 * @param <T> is the type of the elements.
	 * @param clazz is the type of the elements.
	 * @param source is the array to scan
	 * @param to_remove are the elements to remove.
	 * @return the array without the removed elements.
	 */
	public static <T> T[] removeElements(Class<T> clazz, T[] source, @SuppressWarnings("unchecked") T... to_remove) {
		ArrayList<T> list = new ArrayList<>();
		list.addAll(Arrays.asList(source));
		for (T t : to_remove) {
			list.remove(t);
		}
		return toArrayAndClear(list,clazz);
	}
	
	/** Cast the specified array and put <code>null</code> is the array when
	 * the element could not be casted.
	 * 
	 * @param <TO> is the type of the elements before the cast.
	 * @param <TT> is the type of the elements after the cast.
	 * @param original_array is the array to cast
	 * @param clazz is the casting type
	 * @return the array in which each element was casted according to the given type.
	 * @see #castRestrictedArray(Object[], Class)
	 */
	public static <TO,TT> TT[] castArray(TO[] original_array, Class<TT> clazz) {
		int l = (original_array==null) ? 0 : original_array.length;
		TT[] result = newInstance(clazz,l);
		if ((original_array!=null)&&(l>0)) {
			int index = 0;
			for (TO to : original_array) {
				result[index] = (clazz.isInstance(to)) ? clazz.cast(to) : null;
			}
		}
		return result;
	}

	/** Cast the specified array and put <code>null</code> is the array when
	 * the element could not be casted.
	 * 
	 * @param <TO> is the type of the elements before the cast.
	 * @param <TT> is the type of the elements after the cast.
	 * @param original_array is the array to cast
	 * @param clazz is the casting type
	 * @return the array in which each element was casted according to the given type.
	 * @see #castRestrictedArray(Object[], Class)
	 */
	public static <TO,TT> TT[] castArray(Collection<TO> original_array, Class<TT> clazz) {
		int l = (original_array==null) ? 0 : original_array.size();
		TT[] result = newInstance(clazz,l);
		if ((original_array!=null)&&(l>0)) {
			int index = 0;
			for (TO to : original_array) {
				result[index] = (clazz.isInstance(to)) ? clazz.cast(to) : null;
			}
		}
		return result;
	}

	/** Replies an array in which all the elements must 
	 * respect the given comparator.
	 * <p>
	 * The respect of the comparator is done when the
	 * comparator replies equals.
	 * 
	 * @param <T> is the type of the elements
	 * @param original_array is the array to cast
	 * @param clazz is the casting type
	 * @param comparator is filtering the elements.
	 * @return the array in which each element was casted according to the given type.
	 */
	public static <T> T[] restrictArray(T[] original_array, Class<T> clazz, Filter<T> comparator) {
		int l = (original_array==null) ? 0 : original_array.length;
		final ArrayList<T> result = new ArrayList<>();
		if ((original_array!=null)&&(l>0)) {
			for (T to : original_array) {
				if (comparator.filter(to)) {
					result.add(to);
				}
			}
		}
		return toArrayAndClear(result, clazz);
	}

	/** Cast the specified array and remove the elements that could not be casted.
	 * 
	 * @param <TO> is the type of the elements before the cast.
	 * @param <TT> is the type of the elements after the cast.
	 * @param original_array is the array to cast
	 * @param clazz is the casting type
	 * @return the array in which each element was casted according to the given type.
	 * @see #castArray(Object[], Class)
	 */
	@SuppressWarnings("unchecked")
	public static <TO,TT> TT[] castRestrictedArray(TO[] original_array, Class<TT> clazz) {
		int l = (original_array==null) ? 0 : original_array.length;
		final ArrayList<TT> result = new ArrayList<>();
		if ((original_array!=null)&&(l>0)) {
			for (TO to : original_array) {
				if (clazz.isInstance(to)) {
					result.add((TT)to);
				}
			}
		}
		return toArrayAndClear(result, clazz);
	}

	/** Cast the specified array and remove the elements that could not be casted.
	 * 
	 * @param <TO> is the type of the elements before the cast.
	 * @param <TT> is the type of the elements after the cast.
	 * @param original_array is the array to cast
	 * @param clazz is the casting type
	 * @return the array in which each element was casted according to the given type.
	 * @see #castArray(Object[], Class)
	 */
	@SuppressWarnings("unchecked")
	public static <TO,TT> TT[] castRestrictedArray(Collection<TO> original_array, Class<TT> clazz) {
		int l = (original_array==null) ? 0 : original_array.size();
		final ArrayList<TT> result = new ArrayList<>();
		if ((original_array!=null)&&(l>0)) {
			for (TO to : original_array) {
				if (clazz.isInstance(to)) {
					result.add((TT)to);
				}
			}
		}
		return toArrayAndClear(result, clazz);
	}

	/** Replies of the given element is in the array.
	 * <p>
	 * This function does not call {@link Object#equals(java.lang.Object)}.
	 * It tests the equality on the object references.
	 * 
	 * @param <T> is the type of the elements.
	 * @param elt is the element to search for.
	 * @param array is the array inside which the search must be done.
	 * @return <code>true</code> if the element is inside the array, otherwise <code>false</code>
	 */
	public static <T> boolean containsObject(T elt, T[] array) {
		for (T t : array) {
			if (t==elt) return true;
		}
		return false;
	}

	/** Replies of the given element is in the array.
	 * <p>
	 * This function does not call {@link Object#equals(java.lang.Object)}.
	 * It tests the equality on the object references.
	 * 
	 * @param <T> is the type of the elements.
	 * @param elts are the elements to search for.
	 * @param array is the array inside which the search must be done.
	 * @return <code>true</code> if the elements are inside the array, otherwise <code>false</code>
	 */
	public static <T> boolean containsAllObjects(T[] elts, T[] array) {
		boolean found;
		for (T elt : elts) {
			found = false;
			for (T t : array) {
				if (t==elt) {
					found = true;
					break;
				}
			}
			if (!found) return false;
		}
		return true;
	}

	/** Replies of the given element is in the array.
	 * <p>
	 * This function is based on {@link Object#equals(java.lang.Object)}.
	 * 
	 * @param <T> is the type of the elements.
	 * @param elt is the element to search for.
	 * @param array is the array inside which the search must be done.
	 * @return <code>true</code> if the element is inside the array, otherwise <code>false</code>
	 */
	public static <T> boolean contains(T elt, @SuppressWarnings("unchecked") T... array) {
		for (T t : array) {
			if ((t==elt)||
				((t!=null)&&
				 (t.equals(elt)))) {
				return true;
			}
		}
		return false;
	}
	
	/** Replies of the given element is in the sorted array.
	 * <p>
	 * This function assumes that the given array is sorted
	 * according to the given comparator.
	 * A dichotomic algorithm is used.
	 * 
	 * @param <T> is the type of the elements.
	 * @param comparator is the comparator used to sort the array.
	 * @param elt is the element to search for.
	 * @param array is the array inside which the search must be done.
	 * @return <code>true</code> if the element is inside the array, otherwise <code>false</code>
	 * @since 4.0
	 */
	public static <T> boolean contains(Comparator<T> comparator, T elt, @SuppressWarnings("unchecked") T... array) {
		assert(comparator!=null);
		assert(elt!=null);
		assert(array!=null);
		
		int f = 0;
		int l = array.length-1;
		int c, cmp;
		T indata;
		while (l>=f) {
			c = (f+l)/2;
			indata = array[c];
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
		return false;
	}

	/** Replies if the given elements is in the array.
	 * <p>
	 * This function is based on {@link Object#equals(java.lang.Object)}.
	 * 
	 * @param <T> is the type of the elements.
	 * @param elts are the elements to search for.
	 * @param array is the array inside which the search must be done.
	 * @return <code>true</code> if the elements are inside the array, otherwise <code>false</code>
	 */
	public static <T> boolean containsAll(T[] elts, T[] array) {
		boolean found;
		for (T elt : elts) {
			found = false;
			for (T t : array) {
				if ((t==elt)||
					((t!=null)&&(t.equals(elt)))) {
					found = true;
					break;
				}
			}
			if (!found) return false;
		}
		return true;
	}

	/** Replies of one of the given elements is in the array.
	 * <p>
	 * This function is based on {@link Object#equals(java.lang.Object)}.
	 * 
	 * @param <T> is the type of the elements.
	 * @param elts is the first array.
	 * @param array is the second array.
	 * @return <code>true</code> if an intersection is existing, otherwise <code>false</code>
	 */
	public static <T> boolean intersects(T[] elts, T[] array) {
		for (T t : array) {
			for (T elt : elts) {
				if ((elt==t)||
					((t!=null)&&
					 (t.equals(elt)))) {
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
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] newInstance(Class<T> clazz, int size) {
		if (size<0) throw new IndexOutOfBoundsException(size+"<0"); //$NON-NLS-1$
		return (T[])Array.newInstance(clazz, size);
	}

	/** Shuffle the specified array.
	 * 
	 * @param <T> is the type of the elements.
	 * @param array is the array to shuffle.
	 */
	public static <T> void shuffle(T[] array) {
        shuffle(array,new Random());
	}

    /** Shuffle the specified array.
	 * 
	 * @param <T> is the type of the elements.
	 * @param array is the array to shuffle.
	 * @param rnd is the random number generator to use.
	 */
	public static <T> void shuffle(T[] array, Random rnd) {
		int ir;
		T tmp;
        for (int i=array.length; i>1; --i) {
        	ir = rnd.nextInt(i);
            tmp = array[i-1];
            array[i-1] = array[ir];
            array[ir] = tmp;
        }
	}

    /** Shuffle the specified array.
	 * 
	 * @param array is the array to shuffle.
	 */
	public static void shuffle(boolean[] array) {
		shuffle(array, new Random());
	}

	/** Shuffle the specified array.
	 * 
	 * @param array is the array to shuffle.
	 * @param rnd is the random number generator to use.
	 */
	public static void shuffle(boolean[] array, Random rnd) {
		int ir;
		boolean tmp;
        for (int i=array.length; i>1; --i) {
        	ir = rnd.nextInt(i);
            tmp = array[i-1];
            array[i-1] = array[ir];
            array[ir] = tmp;
        }
	}

    /** Shuffle the specified array.
	 * 
	 * @param array is the array to shuffle.
	 */
	public static void shuffle(byte[] array) {
		shuffle(array,new Random());
	}

	/** Shuffle the specified array.
	 * 
	 * @param array is the array to shuffle.
	 * @param rnd is the random number generator to use.
	 */
	public static void shuffle(byte[] array, Random rnd) {
		int ir;
		byte tmp;
        for (int i=array.length; i>1; --i) {
        	ir = rnd.nextInt(i);
            tmp = array[i-1];
            array[i-1] = array[ir];
            array[ir] = tmp;
        }
	}

    /** Shuffle the specified array.
	 * 
	 * @param array is the array to shuffle.
	 */
	public static void shuffle(char[] array) {
		shuffle(array,new Random());
	}

	/** Shuffle the specified array.
	 * 
	 * @param array is the array to shuffle.
	 * @param rnd is the random number generator to use.
	 */
	public static void shuffle(char[] array, Random rnd) {
		int ir;
		char tmp;
        for (int i=array.length; i>1; --i) {
        	ir = rnd.nextInt(i);
            tmp = array[i-1];
            array[i-1] = array[ir];
            array[ir] = tmp;
        }
	}

    /** Shuffle the specified array.
	 * 
	 * @param array is the array to shuffle.
	 */
	public static void shuffle(int[] array) {
		shuffle(array,new Random());
	}

	/** Shuffle the specified array.
	 * 
	 * @param array is the array to shuffle.
	 * @param rnd is the random number generator to use.
	 */
	public static void shuffle(int[] array, Random rnd) {
		int ir;
		int tmp;
        for (int i=array.length; i>1; --i) {
        	ir = rnd.nextInt(i);
            tmp = array[i-1];
            array[i-1] = array[ir];
            array[ir] = tmp;
        }
	}

    /** Shuffle the specified array.
	 * 
	 * @param array is the array to shuffle.
	 */
	public static void shuffle(long[] array) {
		shuffle(array,new Random());
	}

	/** Shuffle the specified array.
	 * 
	 * @param array is the array to shuffle.
	 * @param rnd is the random number generator to use.
	 */
	public static void shuffle(long[] array, Random rnd) {
		int ir;
		long tmp;
        for (int i=array.length; i>1; --i) {
        	ir = rnd.nextInt(i);
            tmp = array[i-1];
            array[i-1] = array[ir];
            array[ir] = tmp;
        }
	}

    /** Shuffle the specified array.
	 * 
	 * @param array is the array to shuffle.
	 */
	public static void shuffle(float[] array) {
		shuffle(array,new Random());
	}

	/** Shuffle the specified array.
	 * 
	 * @param array is the array to shuffle.
	 * @param rnd is the random number generator to use.
	 */
	public static void shuffle(float[] array, Random rnd) {
		int ir;
		float tmp;
        for (int i=array.length; i>1; --i) {
        	ir = rnd.nextInt(i);
            tmp = array[i-1];
            array[i-1] = array[ir];
            array[ir] = tmp;
        }
	}

    /** Shuffle the specified array.
	 * 
	 * @param array is the array to shuffle.
	 */
	public static void shuffle(double[] array) {
		shuffle(array,new Random());
	}

	/** Shuffle the specified array.
	 * 
	 * @param array is the array to shuffle.
	 * @param rnd is the random number generator to use.
	 */
	public static void shuffle(double[] array, Random rnd) {
		int ir;
		double tmp;
        for (int i=array.length; i>1; --i) {
        	ir = rnd.nextInt(i);
            tmp = array[i-1];
            array[i-1] = array[ir];
            array[ir] = tmp;
        }
	}

	/**
	 * Replies a string representation of the given object.
	 * <p>
	 * This function supports the base type's arrays.
	 * 
	 * @param o is the object to translate.
	 * @return a string representation of the given object.
	 */
	public static String toString(Object o) {
		if (o==null) return null;
		if (o instanceof boolean[])
			return Arrays.toString((boolean[])o);
		if (o instanceof byte[])
			return Arrays.toString((byte[])o);
		if (o instanceof char[])
			return Arrays.toString((char[])o);
		if (o instanceof short[])
			return Arrays.toString((short[])o);
		if (o instanceof int[])
			return Arrays.toString((int[])o);
		if (o instanceof long[])
			return Arrays.toString((long[])o);
		if (o instanceof float[])
			return Arrays.toString((float[])o);
		if (o instanceof double[])
			return Arrays.toString((double[])o);
		if (o instanceof Object[])
			return Arrays.toString((Object[])o);
		return o.toString();
	}

	/** Replies a sized iterator on the objects.
	 * 
	 * @param <T> is the type of the object to iterate on.
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 */
	public static <T> SizedIterator<T> sizedIterator(T[] array) {
		return new ArraySizedIterator<>(array);
	}

	/** Replies a sized iterator on the objects.
	 * 
	 * @param <T> is the type of the object to iterate on.
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	public static <T> Iterator<T> iterator(T[] array) {
		return new ArraySizedIterator<>(array);
	}

	/** Replies a sized iterator on the objects.
	 * 
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	public static SizedIterator<Boolean> sizedIterator(boolean[] array) {
		return new NativeBooleanToObjectBooleanIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 * 
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	public static Iterator<Boolean> iterator(boolean[] array) {
		return new NativeBooleanToObjectBooleanIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 * 
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	public static SizedIterator<Character> sizedIterator(char[] array) {
		return new NativeCharacterToObjectCharacterIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 * 
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	public static Iterator<Character> iterator(char[] array) {
		return new NativeCharacterToObjectCharacterIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 * 
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	public static SizedIterator<Byte> sizedIterator(byte[] array) {
		return new NativeByteToObjectByteIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 * 
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	public static Iterator<Byte> iterator(byte[] array) {
		return new NativeByteToObjectByteIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 * 
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	public static SizedIterator<Short> sizedIterator(short[] array) {
		return new NativeShortToObjectShortIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 * 
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	public static Iterator<Short> iterator(short[] array) {
		return new NativeShortToObjectShortIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 * 
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	public static SizedIterator<Integer> sizedIterator(int[] array) {
		return new NativeIntegerToObjectIntegerIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 * 
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	public static Iterator<Integer> iterator(int[] array) {
		return new NativeIntegerToObjectIntegerIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 * 
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	public static SizedIterator<Long> sizedIterator(long[] array) {
		return new NativeLongToObjectLongIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 * 
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	public static Iterator<Long> iterator(long[] array) {
		return new NativeLongToObjectLongIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 * 
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	public static SizedIterator<Float> sizedIterator(float[] array) {
		return new NativeFloatToObjectFloatIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 * 
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	public static Iterator<Float> iterator(float[] array) {
		return new NativeFloatToObjectFloatIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 * 
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	public static SizedIterator<Double> sizedIterator(double[] array) {
		return new NativeDoubleToObjectDoubleIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 * 
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	public static Iterator<Double> iterator(double[] array) {
		return new NativeDoubleToObjectDoubleIterator(array);
	}

	/**
	 * Some utilities functions for arrays. 
	 *
	 * @param <T> is the type of the value to pass to the filter.
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static interface Filter<T> {
		
		/**
		 * @param o is the value to filter
		 * @return <code>true</code> if the given value is acceptable, otherwise <code>false</code>
		 */
		public boolean filter(T o);
		
	}	
	
	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavenartifactid $ArtifactId$
	 * @mavengroupid $GroupId$
	 */
	private static class NativeBooleanToObjectBooleanIterator implements SizedIterator<Boolean> {

		private final boolean[] array;
		private int idx = 0;
		
		/**
		 * @param data
		 */
		public NativeBooleanToObjectBooleanIterator(boolean[] data) {
			this.array = data;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return this.array!=null &&  this.idx>=0 && this.idx<this.array.length;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Boolean next() {
			if (this.array!=null && this.idx>=0 && this.idx<this.array.length) {
				Boolean b = Boolean.valueOf(this.array[this.idx]);
				++this.idx;
				return b;
			}
			throw new NoSuchElementException();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int totalSize() {
			return this.array==null ? 0 : this.array.length;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int index() {
			return this.idx - 1;
		}
		
		@Override
		public int rest() {
			return totalSize() - (index()+1);
		}

	} // class NativeBooleanToObjectBooleanIterator

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavenartifactid $ArtifactId$
	 * @mavengroupid $GroupId$
	 */
	private static class NativeCharacterToObjectCharacterIterator implements SizedIterator<Character> {

		private final char[] array;
		private int idx = 0;
		
		/**
		 * @param data
		 */
		public NativeCharacterToObjectCharacterIterator(char[] data) {
			this.array = data;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return this.array!=null &&  this.idx>=0 && this.idx<this.array.length;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Character next() {
			if (this.array!=null && this.idx>=0 && this.idx<this.array.length) {
				Character c = Character.valueOf(this.array[this.idx]);
				++this.idx;
				return c;
			}
			throw new NoSuchElementException();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int totalSize() {
			return this.array==null ? 0 : this.array.length;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int index() {
			return this.idx - 1;
		}
		
		@Override
		public int rest() {
			return totalSize() - (index()+1);
		}

	} // class NativeCharacterToObjectCharacterIterator

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavenartifactid $ArtifactId$
	 * @mavengroupid $GroupId$
	 */
	private static class NativeByteToObjectByteIterator implements SizedIterator<Byte> {

		private final byte[] array;
		private int idx = 0;
		
		/**
		 * @param data
		 */
		public NativeByteToObjectByteIterator(byte[] data) {
			this.array = data;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return this.array!=null &&  this.idx>=0 && this.idx<this.array.length;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Byte next() {
			if (this.array!=null && this.idx>=0 && this.idx<this.array.length) {
				Byte b = Byte.valueOf(this.array[this.idx]);
				++this.idx;
				return b;
			}
			throw new NoSuchElementException();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int totalSize() {
			return this.array==null ? 0 : this.array.length;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int index() {
			return this.idx - 1;
		}
		
		@Override
		public int rest() {
			return totalSize() - (index()+1);
		}

	} // class NativeByteToObjectByteIterator

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavenartifactid $ArtifactId$
	 * @mavengroupid $GroupId$
	 */
	private static class NativeShortToObjectShortIterator implements SizedIterator<Short> {

		private final short[] array;
		private int idx = 0;
		
		/**
		 * @param data
		 */
		public NativeShortToObjectShortIterator(short[] data) {
			this.array = data;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return this.array!=null &&  this.idx>=0 && this.idx<this.array.length;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Short next() {
			if (this.array!=null && this.idx>=0 && this.idx<this.array.length) {
				Short s = Short.valueOf(this.array[this.idx]);
				++this.idx;
				return s;
			}
			throw new NoSuchElementException();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int totalSize() {
			return this.array==null ? 0 : this.array.length;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int index() {
			return this.idx - 1;
		}
		
		@Override
		public int rest() {
			return totalSize() - (index()+1);
		}

	} // class NativeShortToObjectShortIterator
	
	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavenartifactid $ArtifactId$
	 * @mavengroupid $GroupId$
	 */
	private static class NativeIntegerToObjectIntegerIterator implements SizedIterator<Integer> {

		private final int[] array;
		private int idx = 0;
		
		/**
		 * @param data
		 */
		public NativeIntegerToObjectIntegerIterator(int[] data) {
			this.array = data;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return this.array!=null &&  this.idx>=0 && this.idx<this.array.length;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Integer next() {
			if (this.array!=null && this.idx>=0 && this.idx<this.array.length) {
				Integer f = Integer.valueOf(this.array[this.idx]);
				++this.idx;
				return f;
			}
			throw new NoSuchElementException();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int totalSize() {
			return this.array==null ? 0 : this.array.length;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int index() {
			return this.idx - 1;
		}
		
		@Override
		public int rest() {
			return totalSize() - (index()+1);
		}

	} // class NativeIntegerToObjectIntegerIterator

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavenartifactid $ArtifactId$
	 * @mavengroupid $GroupId$
	 */
	private static class NativeLongToObjectLongIterator implements SizedIterator<Long> {

		private final long[] array;
		private int idx = 0;
		
		/**
		 * @param data
		 */
		public NativeLongToObjectLongIterator(long[] data) {
			this.array = data;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return this.array!=null &&  this.idx>=0 && this.idx<this.array.length;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Long next() {
			if (this.array!=null && this.idx>=0 && this.idx<this.array.length) {
				Long l = Long.valueOf(this.array[this.idx]);
				++this.idx;
				return l;
			}
			throw new NoSuchElementException();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int totalSize() {
			return this.array==null ? 0 : this.array.length;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int index() {
			return this.idx - 1;
		}
		
		@Override
		public int rest() {
			return totalSize() - (index()+1);
		}

	} // class NativeLongToObjectLongIterator

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavenartifactid $ArtifactId$
	 * @mavengroupid $GroupId$
	 */
	private static class NativeFloatToObjectFloatIterator implements SizedIterator<Float> {

		private final float[] array;
		private int idx = 0;
		
		/**
		 * @param data
		 */
		public NativeFloatToObjectFloatIterator(float[] data) {
			this.array = data;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return this.array!=null &&  this.idx>=0 && this.idx<this.array.length;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Float next() {
			if (this.array!=null && this.idx>=0 && this.idx<this.array.length) {
				Float f = Float.valueOf(this.array[this.idx]);
				++this.idx;
				return f;
			}
			throw new NoSuchElementException();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int totalSize() {
			return this.array==null ? 0 : this.array.length;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int index() {
			return this.idx - 1;
		}
		
		@Override
		public int rest() {
			return totalSize() - (index()+1);
		}

	} // class NativeFloatToObjectFloatIterator

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavenartifactid $ArtifactId$
	 * @mavengroupid $GroupId$
	 */
	private static class NativeDoubleToObjectDoubleIterator implements SizedIterator<Double> {

		private final double[] array;
		private int idx = 0;
		
		/**
		 * @param data
		 */
		public NativeDoubleToObjectDoubleIterator(double[] data) {
			this.array = data;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return this.array!=null &&  this.idx>=0 && this.idx<this.array.length;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Double next() {
			if (this.array!=null && this.idx>=0 && this.idx<this.array.length) {
				Double d = Double.valueOf(this.array[this.idx]);
				++this.idx;
				return d;
			}
			throw new NoSuchElementException();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int totalSize() {
			return this.array==null ? 0 : this.array.length;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int index() {
			return this.idx - 1;
		}
		
		@Override
		public int rest() {
			return totalSize() - (index()+1);
		}
		
	} // class NativeDoubleToObjectDoubleIterator

}
