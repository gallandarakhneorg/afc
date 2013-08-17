/* 
 * $Id$
 * 
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

package org.arakhne.vmutil.caller;

import org.arakhne.vmutil.ClassLoaderFinder;

/**
 * This utility class provides a way to determine which class
 * call a function.
 * <p>
 * It inspirated from the Andriod API.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class StackTraceCaller implements Caller {

	/** Load a class but avoid any exception.
     *
     * @param name the class name.
     * @return the class or {@code null}.
     */
	public static Class<?> loadClass(String name) {
		try {
			return ClassLoaderFinder.findClassLoader().loadClass(name);
		}
		catch(AssertionError e) {
			throw e;
		}
		catch(Throwable e) {
			return null;
		}
	}

	/** Replies the stack trace element for the given level.
     * <p>
     * The given <var>level</var> permits to specify which class to reply:
     * <ul>
     * <li>{@code 0}: the class where is defined the function ({@code f<sub>0</sub>})
     * that has called one function of {@code Caller}</li>
     * <li>{@code 1}: the class where is defined the function ({@code f<sub>1</sub>})
     * that has called {@code f<sub>0</sub>}</li>
     * <li>{@code 2}: the class where is defined the function ({@code f<sub>2</sub>})
     * that has called {@code f<sub>1</sub>}</li>
     * <li>etc.</li>
     * </ul>
     *
     * @param level is the desired level.
     * @return the stack trace element; or {@code null}.
     */
	protected static StackTraceElement getTraceElementAt(int level) {
		if (level<0) return null;
		try {
			StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
			int j = -1;
			boolean found = false;
			Class<?> type;
			for(int i=0; i<stackTrace.length; ++i) {
				if (found) {
					if (i-j==level) return stackTrace[i];
				}
				else {
					type = loadClass(stackTrace[i].getClassName());
					if (type!=null && Caller.class.isAssignableFrom(type)) {
						j = i+1;
					}
					else if (j>=0) {
						// First ocurrence of a class in the stack, after the
						// inner invocation of StackTraceCaller
						found = true;
						if (i-j==level) return stackTrace[i];
					}
				}
			}
		}
		catch(AssertionError e) {
			throw e;
		}
		catch(Throwable _) {
			//
		}
		return null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Class<?> getCallerClass(int level) {
		StackTraceElement element = getTraceElementAt(level);
		if (element==null) throw new IllegalArgumentException();
		Class<?> type = loadClass(element.getClassName());
		if (type==null) throw new IllegalArgumentException();
		return type;
	}

	/** {@inheritDoc}
	 */
	@Override
	public String getCallerMethod(int level) {
		StackTraceElement element = getTraceElementAt(level);
		if (element==null) throw new IllegalArgumentException();
		return element.getMethodName();
	}

	/** {@inheritDoc}
	 */
	@Override
	public long getCallerLine(int level) {
		StackTraceElement element = getTraceElementAt(level);
		if (element==null) throw new IllegalArgumentException();
		return element.getLineNumber();
	}

	/** {@inheritDoc}
	 */
	@Override
	public String getCallerFilename(int level) {
		StackTraceElement element = getTraceElementAt(level);
		if (element==null) throw new IllegalArgumentException();
		return element.getFileName();
	}

}
