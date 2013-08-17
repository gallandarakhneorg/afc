/* $Id$
 * 
 * Copyright (C) 2007-09 Stephane GALLAND.
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
package org.arakhne.util.ref;

import java.awt.geom.Point2D;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

/** Test case with utility functions.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid org.arakhne.afc
 * @mavenartifactid arakhneRefs
 */
public abstract class AbstractTestCase extends TestCase {
	
	private static String arrayToString(Object o) {
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

	/** Replies if the given elements is in the array.
	 * <p>
	 * This function is based on {@link Object#equals(java.lang.Object)}.
	 */
	private static <T> boolean arrayContainsAll(T[] elts, T[] array) {
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

	/**
	 * Format a failure message for invalid value.
	 * 
	 * @param message is the message to reply.
	 * @param expected is the expected object.
	 * @param actual is the actual value of the object.
	 * @return the message
	 */
	protected static String formatFailMessage(String message, Object expected, Object actual) {
		StringBuilder formatted = new StringBuilder();
		if (message!=null) {
			formatted.append(message);
			formatted.append(' ');
		}
		formatted.append("expected:<"); //$NON-NLS-1$
		formatted.append(arrayToString(expected));
		formatted.append("> but was:<"); //$NON-NLS-1$
		formatted.append(arrayToString(actual));
		formatted.append(">"); //$NON-NLS-1$
		return formatted.toString();
	}

	/**
	 * Format a failure message for invalid value.
	 * 
	 * @param message is the first part of the message (optional).
	 * @param msg is the second part of the message (mandatory).
	 * @param actual is the actual value of the object.
	 * @return the message
	 */
	protected static String formatFailMessage(String message, String msg, Object actual) {
		StringBuilder formatted = new StringBuilder();
		if (message!=null) {
			formatted.append(message);
			formatted.append(' ');
		}
		formatted.append(msg);
		formatted.append(" but was:<"); //$NON-NLS-1$
		formatted.append(arrayToString(actual));
		formatted.append(">"); //$NON-NLS-1$
		return formatted.toString();
	}

	/**
	 * Format a failure message for not-expected values.
	 * 
	 * @param message is the message to reply.
	 * @param notexpected is the not-expected object.
	 * @return the message
	 */
	protected static String formatFailNegMessage(String message, Object notexpected) {
		StringBuilder formatted = new StringBuilder();
		if (message!=null) {
			formatted.append(message);
			formatted.append(' ');
		}
		formatted.append("not expected:<"); //$NON-NLS-1$
		formatted.append(arrayToString(notexpected));
		formatted.append("> but the same"); //$NON-NLS-1$
		return formatted.toString();
	}

	/** Asserts that two objects are not equal. If they are
	 * an AssertionFailedError is thrown with the given message.
	 * 
	 * @param message is the error message to put inside the assertion.
	 * @param notexpected is the value which is not expected by the unit test.
	 * @param actual is the actual value of the object in the unit test.
	 */
	protected static void assertNotEquals(String message, Object notexpected, Object actual) {
		if ((notexpected!=actual)&&
			((notexpected==null)
			 ||
			 (!notexpected.equals(actual)))) return;
		fail(formatFailNegMessage(message, notexpected));
	}

	/** Asserts that two objects are not equal. If they are
	 * an AssertionFailedError is thrown with the given message.
	 * 
	 * @param notexpected is the value which is not expected by the unit test.
	 * @param actual is the actual value of the object in the unit test.
	 */
	protected static void assertNotEquals(Object notexpected, Object actual) {
	    assertNotEquals(null, notexpected, actual);
	}
	
	/** Asserts that the actuel object is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown with the given message.
	 * 
	 * @param message is the error message to put inside the assertion.
	 * @param expectedObjects are the set of expected values during the unit test.
	 * @param actual is the actual value of the object in the unit test.
	 */
	protected static void assertEquals(String message, Object[] expectedObjects, Object actual) {
		if ((expectedObjects!=null)&&(expectedObjects.length>0)) {
			for (Object object : expectedObjects) {
				if ((object==null)&&(actual==null)) return;
				if ((object!=null)&&(object.equals(actual))) return;
			}
		}
		fail(formatFailMessage(message, expectedObjects, actual));
	}

	/** Asserts that the actuel object is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 * 
	 * @param expectedObjects are the set of expected values during the unit test.
	 * @param actual is the actual value of the object in the unit test.
	 */
	protected static void assertEquals(Object[] expectedObjects, Object actual) {
	    assertEquals(null, expectedObjects, actual);
	}

	/** Asserts that the actuel object is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown with the given message.
	 *
	 * @param <T> is the type of the array.
	 * @param message is the error message to put inside the assertion.
	 * @param expectedObjects are the set of expected values during the unit test.
	 * @param actual is the actual value of the objects in the unit test.
	 */
	protected static <T> void assertEquals(String message, T[] expectedObjects, T[] actual) {
		if (expectedObjects==actual) return;
		if ((expectedObjects!=null)&&(actual!=null)&&
			(expectedObjects.length==actual.length)) {
			boolean ok = true;
			for(int i=0; i<expectedObjects.length; i++) {
				if ((expectedObjects[i]!=null)||(actual[i]!=null)) {
					if ((expectedObjects[i]==null)||(!expectedObjects[i].equals(actual[i]))) {
						ok = false;
						break;
					}
				}
			}
			if (ok) return;
		}
		fail(formatFailMessage(message, expectedObjects, actual));
	}

	/** Asserts that the actuel object is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 *
	 * @param <T> is the type of the array.
	 * @param expectedObjects are the set of expected values during the unit test.
	 * @param actual is the actual value of the objects in the unit test.
	 */
	protected static <T> void assertEquals(T[] expectedObjects, T[] actual) {
	    assertEquals(null, expectedObjects, actual);
	}

	/** Asserts that the actuel object is not equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown with the given message.
	 *
	 * @param <T> is the type of the array.
	 * @param message is the error message to put inside the assertion.
	 * @param expectedObjects are the set of expected values during the unit test.
	 * @param actual is the actual value of the objects in the unit test.
	 */
	protected static <T> void assertNotEquals(String message, T[] expectedObjects, T[] actual) {
		if (expectedObjects!=actual) {			
			if ((expectedObjects!=null)&&(actual!=null)) {
				if (expectedObjects.length!=actual.length) return;
				boolean ok = true;
				for(int i=0; i<expectedObjects.length; i++) {
					if ((expectedObjects[i]!=null)||(actual[i]!=null)) {
						if ((expectedObjects[i]==null)||(!expectedObjects[i].equals(actual[i]))) {
							ok = false;
							break;
						}
					}
				}
				if (!ok) return;
			}
			else return;
		}
		fail(formatFailMessage(message, expectedObjects, actual));
	}

	/** Asserts that the actuel object is not equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 *
	 * @param <T> is the type of the array.
	 * @param expectedObjects are the set of expected values during the unit test.
	 * @param actual is the actual value of the objects in the unit test.
	 */
	protected static <T> void assertNotEquals(T[] expectedObjects, T[] actual) {
	    assertNotEquals(null, expectedObjects, actual);
	}

	/** Asserts that the actuel object is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 * This assertion function tests the types of its parameters to call the best
	 * {@code assertEquals} function.
	 * 
	 * @param expected is the expected value during the unit test.
	 * @param actual is the actual value of the object during the unit test.
	 * @see #assertEquals(Object, Object)
	 * @see #assertEquals(Object[], Object)
	 * @see #assertEquals(Object[], Object[])
	 */
	protected static void assertEqualsGeneric(Object expected, Object actual) {
		assertEqualsGeneric(null, expected, actual);
	}

	/** Asserts that the actuel object is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 * This assertion function tests the types of its parameters to call the best
	 * {@code assertEquals} function.
	 * 
	 * @param message is the error message to put inside the assertion.
	 * @param expected is the expected value during the unit test.
	 * @param actual is the actual value of the object during the unit test.
	 * @see #assertEquals(Object, Object)
	 * @see #assertEquals(Object[], Object)
	 * @see #assertEquals(Object[], Object[])
	 */
	protected static void assertEqualsGeneric(String message, Object expected, Object actual) {
		if ((expected!=null)&&(actual!=null)&&(expected.getClass().isArray())) {
			if (actual.getClass().isArray())
				assertEquals(message, (Object[])expected, (Object[])actual);
			else
				assertEquals(message, (Object[])expected, actual);
		}
		else assertEquals(message, expected, actual);
	}

	/** Asserts that the actuel similar is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 * 
	 * @param <T> is the type of the values
	 * @param expectedObjects are the expected values during the unit test.
	 * @param actual are the actual values of the objects during the unit test.
	 */
	protected static <T> void assertSimilars(T[] expectedObjects, T[] actual) {
	    assertSimilars(null, expectedObjects, actual);
	}

	/** Asserts that the actuel similar is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 * 
	 * @param <T> is the type of the values
	 * @param message is the error message to put inside the assertion.
	 * @param expectedObjects are the expected values during the unit test.
	 * @param actual are the actual values of the objects during the unit test.
	 */
	protected static <T> void assertSimilars(String message, T[] expectedObjects, T[] actual) {
		if (expectedObjects==actual) return;
		if ((arrayContainsAll(expectedObjects, actual))&&(arrayContainsAll(actual, expectedObjects)))
			return;
		fail(formatFailMessage(message, expectedObjects, actual));
	}

	/** Asserts that the actuel similar is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 * 
	 * @param <T> is the type of the values
	 * @param expectedObjects are the expected values during the unit test.
	 * @param actual are the actual values of the objects during the unit test.
	 */
	protected static <T> void assertSimilars(Collection<T> expectedObjects, Collection<T> actual) {
	    assertSimilars(null, expectedObjects, actual);
	}
	
	/** Asserts that the actuel similar is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 * 
	 * @param <T> is the type of the values
	 * @param message is the error message to put inside the assertion.
	 * @param expectedObjects are the expected values during the unit test.
	 * @param actual are the actual values of the objects during the unit test.
	 */
	protected static <T> void assertSimilars(String message, Collection<T> expectedObjects, Collection<T> actual) {
		if (expectedObjects==actual) return;
		if (similars(expectedObjects,actual)) return;
		fail(formatFailMessage(message, expectedObjects, actual));
	}
	
	private static <T> boolean similars(Collection<T> c1, Collection<T> c2) {
		ArrayList<T> a = new ArrayList<T>();
		a.addAll(c2);
		for(T elt : c1) {
			if (!a.remove(elt)) {
				return false;
			}
		}
		return a.isEmpty();
	}

	/** Asserts that the actuel similar is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 * 
	 * @param <T> is the type of the values
	 * @param expectedObjects are the expected values during the unit test.
	 * @param actual are the actual values of the objects during the unit test.
	 */
	protected static <T> void assertEquals(List<T> expectedObjects, List<T> actual) {
	    assertEquals(null, expectedObjects, actual);
	}
	
	/** Asserts that the actuel similar is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 * 
	 * @param <T> is the type of the values.
	 * @param message is the error message to put inside the assertion.
	 * @param expectedObjects are the expected values during the unit test.
	 * @param actual are the actual values of the objects during the unit test.
	 */
	protected static <T> void assertEquals(String message, List<T> expectedObjects, List<T> actual) {
		if (expectedObjects==actual) return;
		if (equals(expectedObjects,actual)) return;
		fail(formatFailMessage(message, expectedObjects, actual));
	}

	private static <T> boolean equals(List<T> c1, List<T> c2) {
		if (c1.size()!=c2.size()) return false;
		int count = c1.size();
		T e1, e2;
		for(int i=0; i<count; i++) {
			e1 = c1.get(i);
			e2 = c2.get(i);
			if ((e1!=e2)&&(!e1.equals(e2))) {
				return false;
			}
		}
		return true;
	}
	
	/** Asserts that the actuel similar is not equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 * 
	 * @param <T> is the type of the values
	 * @param expectedObjects are the expected values during the unit test.
	 * @param actual are the actual values of the objects during the unit test.
	 */
	protected static <T> void assertNotEquals(List<T> expectedObjects, List<T> actual) {
	    assertNotEquals(null, expectedObjects, actual);
	}
	
	/** Asserts that the actuel object is not equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 * 
	 * @param <T> is the type of the values
	 * @param message is the error message to put inside the assertion.
	 * @param expectedObjects are the expected values during the unit test.
	 * @param actual are the actual values of the objects during the unit test.
	 */
	protected static <T> void assertNotEquals(String message, List<T> expectedObjects, List<T> actual) {
		if ((expectedObjects!=actual)&&(!equals(expectedObjects,actual))) return;
		fail(formatFailMessage(message, expectedObjects, actual));
	}

	/** Asserts that the actual object is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 * 
	 * @param <T> is the type of the values
	 * @param <X> is the element's type of the values if they are arrays.
	 * @param message is the error message to put inside the assertion.
	 * @param expected is the expected value during the unit test.
	 * @param actual are the actual value of the object during the unit test.
	 */
	@SuppressWarnings("unchecked")
	protected static <T, X> void assertSimilars(String message, T expected, T actual) {
		if (expected==actual) return;
		if (expected instanceof Collection)
			assertSimilars(message, (Collection<?>)expected, (Collection<?>)actual);
		else if (expected instanceof Point2D)
			assertSimilars(message, (Point2D)expected, (Point2D)actual);
		else if (expected instanceof Date)
			assertSimilars(message, (Date)expected, (Date)actual);
		else if (expected.getClass().isArray())
			assertSimilars(message, (X[])expected, (X[])actual);
		else
			assertEquals(message, expected, actual);
	}

	/** Asserts that the actual object is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 * 
	 * @param <T> is the type of the values.
	 * @param <X> is the element's type of the values if they are arrays.
	 * @param message is the error message to put inside the assertion.
	 * @param expected is the expected value.
	 * @param actual is the current value.
	 */
	protected static <T, X> void assertNotSimilars(String message, T expected, T actual) {
		if (expected!=actual) {
			try {
				assertSimilars(message,expected, actual);
			}
			catch(AssertionError _) {
				// ok
				return;
			}
			catch(AssertionFailedError _) {
				// ok
				return;
			}
		}		
		fail(formatFailMessage(message, expected, actual));
	}

	/** Replies if the two objects are similars.
	 * 
	 * @param <T> is the type of the values
	 * @param <X> is the element's type of the values if they are arrays.
	 * @param obj1
	 * @param obj2
	 * @return <code>true</code> if the objects are similar, otherwise <code>false</code>
	 */
	protected static <T, X> boolean isSimilarObjects(T obj1, T obj2) {
		if (obj1==obj2) return true;
		try {
			assertSimilars(null,obj1, obj2);
			return true;
		}
		catch(AssertionError _) {
			return false;
		}
		catch(AssertionFailedError _) {
			return false;
		}
	}

	/** Asserts that the actuel similar is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 * 
	 * @param <K> is the type of the map keys.
	 * @param <V> is the type of the map values.
	 * @param expectedObjects is the expected map.
	 * @param actual is the current map.
	 */
	protected static <K,V> void assertDeepSimilars(Map<K,V> expectedObjects, Map<K,V> actual) {
	    assertDeepSimilars(null, expectedObjects, actual);
	}
	
	/** Asserts that the actuel similar is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 * 
	 * @param <K> is the type of the map keys.
	 * @param <V> is the type of the map values.
	 * @param message is the error message to put inside the assertion.
	 * @param expectedObjects is the expected map.
	 * @param actual is the current map.
	 */
	protected static <K,V> void assertDeepSimilars(String message, Map<K,V> expectedObjects, Map<K,V> actual) {
		if (expectedObjects==actual) return;
		if (similars(expectedObjects.keySet(), actual.keySet())) {
			for(Entry<K,V> entry : expectedObjects.entrySet()) {
				V v1 = entry.getValue();
				V v2 = actual.get(entry.getKey());
				assertSimilars(message, v1, v2);
			}
			// all values are correct
			return;
		}
		fail(formatFailMessage(message, expectedObjects, actual));
	}

	/** Asserts that the actuel similar is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 * 
	 * @param <K> is the type of the map keys.
	 * @param <V> is the type of the map values.
	 * @param expectedObjects is the expected map.
	 * @param actual is the current map.
	 */
	protected static <K,V> void assertNotDeepSimilars(Map<K,V> expectedObjects, Map<K,V> actual) {
	    assertNotDeepSimilars(null, expectedObjects, actual);
	}
	
	/** Asserts that the actuel similar is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 * 
	 * @param <K> is the type of the map keys.
	 * @param <V> is the type of the map values.
	 * @param message is the error message to put inside the assertion.
	 * @param expectedObjects is the expected map.
	 * @param actual is the current map.
	 */
	protected static <K,V> void assertNotDeepSimilars(String message, Map<K,V> expectedObjects, Map<K,V> actual) {
		if (expectedObjects!=actual) {
			if (!similars(expectedObjects.keySet(), actual.keySet())) return;
			
			for(Entry<K,V> entry : expectedObjects.entrySet()) {
				V v1 = entry.getValue();
				V v2 = actual.get(entry.getKey());
				if (!isSimilarObjects(v1, v2)) return;
			}
		}
		fail(formatFailMessage(message, expectedObjects, actual));
	}

	/** Asserts that the specified value is stricly negative.
	 * 
	 * @param number
	 */
	protected static void assertStrictlyNegative(Number number) {
		assertStrictlyNegative(null,number);
	}

	/** Asserts that the specified value is stricly negative.
	 * 
	 * @param message is the error message to put inside the assertion.
	 * @param number
	 */
	protected static void assertStrictlyNegative(String message, Number number) {
		if (number.doubleValue()<0.) return;
		fail(formatFailMessage(message, "expected negative value", number)); //$NON-NLS-1$
	}

	/** Asserts that the specified value is stricly positive.
	 * 
	 * @param number
	 */
	protected static void assertStrictlyPositive(Number number) {
		assertStrictlyPositive(null,number);
	}

	/** Asserts that the specified value is stricly positive.
	 * 
	 * @param message is the error message to put inside the assertion.
	 * @param number
	 */
	protected static void assertStrictlyPositive(String message, Number number) {
		if (number.doubleValue()>0.) return;
		fail(formatFailMessage(message, "expected positive value", number)); //$NON-NLS-1$
	}

	/** Asserts that the specified value is negative.
	 * 
	 * @param number
	 */
	protected static void assertNegative(Number number) {
		assertNegative(null,number);
	}

	/** Asserts that the specified value is negative.
	 * 
	 * @param message is the error message to put inside the assertion.
	 * @param number
	 */
	protected static void assertNegative(String message, Number number) {
		if (number.doubleValue()<=0.) return;
		fail(formatFailMessage(message, "expected negative value", number)); //$NON-NLS-1$
	}

	/** Asserts that the specified value is positive.
	 * 
	 * @param number
	 */
	protected static void assertPositive(Number number) {
		assertPositive(null,number);
	}

	/** Asserts that the specified value is positive.
	 * 
	 * @param message is the error message to put inside the assertion.
	 * @param number
	 */
	protected static void assertPositive(String message, Number number) {
		if (number.doubleValue()>=0.) return;
		fail(formatFailMessage(message, "expected positive value", number)); //$NON-NLS-1$
	}

	/**
	 * Replies an array which is containing values randomly selected from the parameter.
	 * Duplicate values are allowed.
	 * 
	 * @param <T> is the type of the values.
	 * @param availableValues is the collection of available values.
	 * @return the selected values (possible duplicated)
	 */
	@SuppressWarnings("unchecked")
	protected static <T> T[] extractRandomValues(T[] availableValues) {
		Random rnd = new Random();
		int count = rnd.nextInt(500);
		ArrayList<T> tab = new ArrayList<T>(count);
		for(int i=0; i<count; i++) {
			tab.add(availableValues[rnd.nextInt(availableValues.length)]);
		}
		Class<?> clazz = availableValues.getClass().getComponentType();
		T[] array = (T[])Array.newInstance(clazz, tab.size());
		tab.toArray(array);
		tab.clear();
		return array;
	}
	
	/** Replies a randomized string.
	 * 
	 * @return a random string.
	 */
	protected static String randomString() {
		return randomString(-1);
	}

	/** Replies a randomized string with a max length.
	 *
	 * @param maxSize is the max length of the string.
	 * @return a random string with a max length.
	 */
	protected static String randomString(int maxSize) {
		Random rnd = new Random();
		StringBuilder b = new StringBuilder();
		int count = rnd.nextInt(maxSize<=0 ? 255 : maxSize-1)+1;
		for(int i=0; i<count; i++) {
			char c = (char)('A' + rnd.nextInt(26));
			b.append(c);
		}
		return b.toString();
	}

	/** Assert the the specified method thrown an exception
	 * 
	 * @param self is the calling object
	 * @param method is the name of the method to invoke
	 * @param types is the parameter types of the method
	 * @param parameters is the parameter values to pass at invocation.
	 */
	protected static void assertException(Object self, String method, Class<?>[] types, Object[] parameters) {
		assertException(null, self, method, types, parameters);
	}

	/** Assert the the specified method thrown an exception
	 * 
	 * @param message is the error message to put in the assertion.
	 * @param self is the calling object
	 * @param method is the name of the method to invoke
	 * @param types is the parameter types of the method
	 * @param parameters is the parameter values to pass at invocation.
	 */
	protected static void assertException(String message, Object self, String method, Class<?>[] types, Object[] parameters) {
		try {
			Class<?> clazz = self.getClass();
			Method methodFunc = clazz.getMethod(method, types);
			methodFunc.invoke(self, parameters);
			fail((message==null ? "" : message)+"An exception was attempted but never thrown."); //$NON-NLS-1$ //$NON-NLS-2$
		}
		catch(Exception _) {
			// Expected behavior
		}
	}

	/** Assert the the specified method thrown an exception
	 * 
	 * @param self is the calling object
	 * @param method is the name of the method to invoke
	 */
	protected static void assertException(Object self, String method) {
		assertException(null, self, method, new Class<?>[0], new Object[0]);
	}

	/** Assert the the specified method thrown an exception
	 * 
	 * @param message is the error message to put in the assertion.
	 * @param self is the calling object
	 * @param method is the name of the method to invoke
	 */
	protected static void assertException(String message, Object self, String method) {
		assertException(message, self, method, new Class<?>[0], new Object[0]);
	}

	/** Test if the two collections contain the same elements without
	 * taking into account the order of the elements in the collections.
	 * 
	 * @param <T>
	 * @param expected
	 * @param actual
	 */
	protected static <T> void assertEpsilonEquals(Collection<? extends T> expected, Collection<? extends T> actual) {
		assertEpsilonEquals(null, expected, actual);
	}

	/** Test if the two collections contain the same elements without
	 * taking into account the order of the elements in the collections.
	 *
	 * @param <T>
	 * @param message
	 * @param expected
	 * @param actual
	 */
	protected static <T> void assertEpsilonEquals(String message, Collection<? extends T> expected, Collection<? extends T> actual) {
		ArrayList<T> l = new ArrayList<T>(actual);
		for(T e : expected) {
			if (!l.remove(e)) {
				fail((message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
						+"not similar collections, expected element:"+ //$NON-NLS-1$
						expected);
			}
		}
		if (!l.isEmpty()) {
			fail((message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
					+"not similar collections, not expected elements:"+ //$NON-NLS-1$
					l);
		}
	}

	/** Test if the two collections contain the same elements without
	 * taking into account the order of the elements in the collections.
	 * 
	 * @param <T>
	 * @param expected
	 * @param actual
	 */
	protected static <T> void assertEpsilonEquals(T[] expected, T[] actual) {
		assertEpsilonEquals(null, expected, actual);
	}

	/** Test if the two collections contain the same elements without
	 * taking into account the order of the elements in the collections.
	 *
	 * @param <T>
	 * @param message
	 * @param expected
	 * @param actual
	 */
	protected static <T> void assertEpsilonEquals(String message, T[] expected, T[] actual) {
		ArrayList<T> l = new ArrayList<T>(Arrays.asList(actual));
		for(T e : expected) {
			if (!l.remove(e)) {
				fail((message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
						+"not similar collections, expected element:"+ //$NON-NLS-1$
						Arrays.toString(expected));
			}
		}
		if (!l.isEmpty()) {
			fail((message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
					+"not similar collections, not expected elements:"+ //$NON-NLS-1$
					l.toString());
		}
	}

	/** Test if the two collections do no contain the same elements without
	 * taking into account the order of the elements in the collections.
	 * 
	 * @param <T>
	 * @param expected
	 * @param actual
	 */
	protected static <T> void assertNotEpsilonEquals(T[] expected, T[] actual) {
		assertNotEpsilonEquals(null, expected, actual);
	}

	/** Test if the two collections do no contain the same elements without
	 * taking into account the order of the elements in the collections.
	 * 
	 * @param <T>
	 * @param message
	 * @param expected
	 * @param actual
	 */
	protected static <T> void assertNotEpsilonEquals(String message, T[] expected, T[] actual) {
		ArrayList<T> l = new ArrayList<T>(Arrays.asList(actual));
		for(T e : expected) {
			if (!l.remove(e)) return;
		}
		if (l.isEmpty()) {
			fail((message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
					+"similar collections when not attempted"); //$NON-NLS-1$
		}
	}

}
