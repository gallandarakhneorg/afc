/* 
 * $Id$
 * 
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (C) 2013 Stephane GALLAND.
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
package org.arakhne.afc.attrs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import junit.framework.TestCase;

/**
 * Test of Attribute.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class AbstractAttrTestCase extends TestCase {

	/**
	 */
	protected final Random RANDOM = new Random();
	
	/**
	 * @param message is the message to format
	 * @param expected is the expected value
	 * @param actual is the current value
	 * @return the formated message
	 */
	static String formatMsg(String message, Object expected, Object actual) {
		String formatted= ""; //$NON-NLS-1$
		if (message != null)
			formatted= message+" "; //$NON-NLS-1$
		return formatted+"expected:<"+expected+"> but was:<"+actual+">"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	/** Create a string.
	 * @return a string
	 */
	protected static String randomString() {
		return UUID.randomUUID().toString();
	}
	
	/** Random a value from an enumeration.
	 * 
	 * @param <E> is the type of the enumeration
	 * @param type is the type of the enumeration
	 * @return a value from the specified enumeration.
	 */
	protected <E extends Enum<E>> E randomEnum(Class<E> type) {
		E[] constants = type.getEnumConstants();
		return constants[this.RANDOM.nextInt(constants.length)];
	}
	
	/** Test if the two date are equals with an epsilon.
	 * 
	 * @param expected
	 * @param actual
	 */
	protected static void assertEpsilonEquals(Date expected, Date actual) {
		if (expected == null && actual == null) return;
		String message = null;
		if (expected!=null) {
			DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");   //$NON-NLS-1$
			String expectedStr = fmt.format(expected);
			String actualStr = fmt.format(expected);
			if (expectedStr.equals(actualStr)) return;
			message = "expected <"+expectedStr+">, actual: <"+actualStr+">"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		fail(formatMsg(message, expected, actual));
	}
	
	/** Test if the two collections contain the same elements and
	 * taking into account the order of the elements in the collections.
	 *
	 * @param <T>
	 * @param expected
	 * @param actual
	 */
	protected static <T> void assertEquals(T[] expected, T[] actual) {
		assertEquals(null, expected, actual);
	}
	
	/** Test if the two collections contain the same elements and
	 * taking into account the order of the elements in the collections.
	 *
	 * @param <T>
	 * @param message
	 * @param expected
	 * @param actual
	 */
	protected static <T> void assertEquals(String message, T[] expected, T[] actual) {
		if (actual==expected) return;
		if (actual==null) {
			fail(formatMsg((message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
					+"not same arrays, not same size",//$NON-NLS-1$
					expected.length, 0));
		}
		else {
			if (expected==null) {
				fail(formatMsg((message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
						+"not same arrays, not same size",//$NON-NLS-1$
						0, actual.length));
			}
			else {
				if (actual.length!=expected.length) {
					fail(formatMsg((message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
							+"not same arrays, not same size",//$NON-NLS-1$
							expected.length, actual.length));
				}
				T o1, o2;
				for(int i=0; i<expected.length; i++) {
					o1 = expected[i];
					o2 = actual[i];
					if ((o1!=o2)&&
						((o1==null)||(o2==null)||
						 (!o1.equals(o2)))) {
						fail(formatMsg((message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
								+"not same arrays, expected element:", //$NON-NLS-1$
								o1, o2));
					}
				}
			}
		}
	}	

	/** Test if the given method entity throws the specified exception on the given entity.
	 * 
	 * @param expectedException
	 * @param object
	 * @param methodName
	 * @param values are the values to pass to the methods.
	 */
	protected static void assertException(Class<? extends Throwable> expectedException,
			Object object, String methodName, Object... values) {
		assertException(null, expectedException, object, methodName, values);
	}

	/** Test if the given method entity throws the specified exception on the given entity.
	 * 
	 * @param message
	 * @param expectedException
	 * @param object
	 * @param methodName
	 * @param values are the values to pass to the methods.
	 */
	protected static void assertException(String message, Class<? extends Throwable> expectedException,
			Object object, String methodName, Object... values) {
		Class<?>[] types = new Class<?>[values.length];
		for(int idx=0; idx<types.length; idx++) {
			types[idx] = values[idx].getClass();
		}
		assertException(message, expectedException, object, methodName, types, values);
	}

	/** Test if the given method entity throws the specified exception on the given entity.
	 * 
	 * @param expectedException
	 * @param object
	 * @param methodName
	 * @param types are the types of the method parameters.
	 * @param values are the values to pass to the methods.
	 */
	protected static void assertException(Class<? extends Throwable> expectedException,
			Object object, String methodName, Class<?>[] types, Object... values) {
		assertException(null, expectedException, object, methodName, types, values);
	}

	/** Test if the given method entity throws the specified exception on the given entity.
	 * 
	 * @param message
	 * @param expectedException
	 * @param object
	 * @param methodName
	 * @param types are the types of the method parameters.
	 * @param values are the values to pass to the methods.
	 */
	protected static void assertException(String message, Class<? extends Throwable> expectedException,
			Object object, String methodName, Class<?>[] types, Object... values) {
		assert(object!=null);
		Class<?> objType;
		Object obj = object;
		if (obj instanceof Class<?>) {
			objType = (Class<?>)obj;
			obj = null;
		}
		else {
			objType = obj.getClass();
		}
		Method method = null;
		Throwable t = null;
		try {
			method = objType.getMethod(methodName, types);
			if (method==null) {
				fail((message!=null?(message+", ") //$NON-NLS-1$
						:"") //$NON-NLS-1$
						+"unable to find the method "+methodName); //$NON-NLS-1$
				return;
			}
		}
		catch(Exception exception) {
			fail((message!=null?(message+", ") //$NON-NLS-1$
					:"") //$NON-NLS-1$
					+"unable to find the method "+methodName); //$NON-NLS-1$
			return;
		}
		try {
			method.invoke(obj, values);
		}
		catch(InvocationTargetException e) {
			if (expectedException.equals(e.getCause().getClass())) return;
			t = e.getCause();
		}
		catch(Throwable e) {
			if (expectedException.equals(e.getClass())) return;
			t = e;
		}
		if (t!=null) {
			fail((message!=null?(message+", ") //$NON-NLS-1$
					:"") //$NON-NLS-1$
					+"the method "+methodName //$NON-NLS-1$
					+" does not thrown the expected exception of type "+expectedException //$NON-NLS-1$
					+". An exception of type "+t.getClass().getName()+" is thrown insteed."); //$NON-NLS-1$ //$NON-NLS-2$
		}
		else {
			fail((message!=null?(message+", ") //$NON-NLS-1$
					:"") //$NON-NLS-1$
					+"the method "+methodName //$NON-NLS-1$
					+" does not thrown the expected exception of type "+expectedException //$NON-NLS-1$
					+". No exception was thrown insteed."); //$NON-NLS-1$
		}
	}

	/** Test if the value is strictly negative.
	 * 
	 * @param value
	 */
	protected static void assertStrictlyNegative(double value) {
		assertStrictlyNegative(null, value);
	}

	/** Test if the value is strictly negative.
	 * 
	 * @param message
	 * @param value
	 */
	protected static void assertStrictlyNegative(String message, double value) {
		if (value>=0) {
			StringBuilder msg = new StringBuilder();
			if (message!=null) msg.append(message);
			if (msg.length()>0) msg.append(' ');
			msg.append("the value is expected to be stricly negative, but it is equals to "); //$NON-NLS-1$
			msg.append(value);
			fail(msg.toString());
		}
	}
	
	/** Test if the value is strictly positive.
	 * 
	 * @param value
	 */
	protected static void assertStrictlyPositive(double value) {
		assertStrictlyPositive(null, value);
	}

	/** Test if the value is strictly positive.
	 * 
	 * @param message
	 * @param value
	 */
	protected static void assertStrictlyPositive(String message, double value) {
		if (value<=0) {
			StringBuilder msg = new StringBuilder();
			if (message!=null) msg.append(message);
			if (msg.length()>0) msg.append(' ');
			msg.append("the value is expected to be stricly positive, but it is equals to "); //$NON-NLS-1$
			msg.append(value);
			fail(msg.toString());
		}
	}

	/** Test if the value is negative or zero.
	 * 
	 * @param value
	 */
	protected static void assertNegative(double value) {
		assertNegative(null, value);
	}

	/** Test if the value is negative or zero.
	 * 
	 * @param message
	 * @param value
	 */
	protected static void assertNegative(String message, double value) {
		if (value>0) {
			StringBuilder msg = new StringBuilder();
			if (message!=null) msg.append(message);
			if (msg.length()>0) msg.append(' ');
			msg.append("the value is expected to be negative or nul, but it is equals to "); //$NON-NLS-1$
			msg.append(value);
			fail(msg.toString());
		}
	}
	
	/** Test if the value is positive or zero.
	 * 
	 * @param value
	 */
	protected static void assertPositive(double value) {
		assertPositive(null, value);
	}

	/** Test if the value is positive or zero.
	 * 
	 * @param message
	 * @param value
	 */
	protected static void assertPositive(String message, double value) {
		if (value<0) {
			StringBuilder msg = new StringBuilder();
			if (message!=null) msg.append(message);
			if (msg.length()>0) msg.append(' ');
			msg.append("the value is expected to be positive or nul, but it is equals to "); //$NON-NLS-1$
			msg.append(value);
			fail(msg.toString());
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
						expected.toString());
			}
		}
		if (!l.isEmpty()) {
			fail((message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
					+"not similar collections, not expected elements:"+ //$NON-NLS-1$
					l.toString());
		}
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

	/**
	 */
	public void testIddle() {
		//
	}	

}
