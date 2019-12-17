/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.vmutil.caller;

import org.arakhne.afc.vmutil.ClassLoaderFinder;

/**
 * This utility class provides a way to determine which class
 * call a function.
 *
 * <p>It inspirated from the Andriod API.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class StackTraceCaller implements Caller {

	private static Class<?> loadClass(String name) {
		try {
			return ClassLoaderFinder.findClassLoader().loadClass(name);
		} catch (AssertionError e) {
			throw e;
		} catch (Throwable e) {
			return null;
		}
	}

	/** Replies the stack trace element for the given level.
	 *
     * <p>The given {@code level} permits to specify which class to reply:
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
		if (level < 0) {
			return null;
		}
		try {
			final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
			int j = -1;
			boolean found = false;
			Class<?> type;
			for (int i = 0; i < stackTrace.length; ++i) {
				if (found) {
					if ((i - j) == level) {
						return stackTrace[i];
					}
				} else {
					type = loadClass(stackTrace[i].getClassName());
					if (type != null && Caller.class.isAssignableFrom(type)) {
						j = i + 1;
					} else if (j >= 0) {
						// First ocurrence of a class in the stack, after the
						// inner invocation of StackTraceCaller
						found = true;
						if ((i - j) == level) {
							return stackTrace[i];
						}
					}
				}
			}
		} catch (AssertionError e) {
			throw e;
		} catch (Throwable exception) {
			//
		}
		return null;
	}

	@Override
	public Class<?> getCallerClass(int level) {
		final StackTraceElement element = getTraceElementAt(level);
		if (element == null) {
			throw new IllegalArgumentException();
		}
		final Class<?> type = loadClass(element.getClassName());
		if (type == null) {
			throw new IllegalArgumentException();
		}
		return type;
	}

	@Override
	public String getCallerMethod(int level) {
		final StackTraceElement element = getTraceElementAt(level);
		if (element == null) {
			throw new IllegalArgumentException();
		}
		return element.getMethodName();
	}

	@Override
	public long getCallerLine(int level) {
		final StackTraceElement element = getTraceElementAt(level);
		if (element == null) {
			throw new IllegalArgumentException();
		}
		return element.getLineNumber();
	}

	@Override
	public String getCallerFilename(int level) {
		final StackTraceElement element = getTraceElementAt(level);
		if (element == null) {
			throw new IllegalArgumentException();
		}
		return element.getFileName();
	}

}
