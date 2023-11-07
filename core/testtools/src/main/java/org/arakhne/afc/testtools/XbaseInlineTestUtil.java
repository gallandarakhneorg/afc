/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.testtools;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Method;

import com.google.common.base.Strings;
import org.eclipse.xtext.xbase.lib.Inline;

/** Class that is testing if {@code @Inline} annotation.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public final class XbaseInlineTestUtil {

	private XbaseInlineTestUtil() {
		//
	}

	/** Replies the {@code @Inline} annotation on the given method.
	 *
	 * @param type the type.
	 * @param methodName the method name.
	 * @param parameters the list of parameter types.
	 * @return the annotation or {@code null}.
	 * @throws RuntimeException in the case the method cannot be found.
	 */
	public static Inline getInlineAnnotation(Class<?> type, String methodName, Class<?>... parameters) {
		try {
			final Method method = type.getDeclaredMethod(methodName, parameters);
			return method.getAnnotation(Inline.class);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	/** Assert that the inline annotation contains all the formal parameters.
	 *
	 * <p>The parameters will be replaced in the expected string by {@code $1}, {@code $2}, etc.
	 *
	 * @param type the type.
	 * @param methodName the method name.
	 * @param parameters the list of parameter types.
	 */
	public static void assertInlineParameterUsage(Class<?> type, String methodName, Class<?>... parameters) {
		final Inline annotation = getInlineAnnotation(type, methodName, parameters);
		if (annotation == null) {
			fail("@Inline annotation not found"); //$NON-NLS-1$
			return;
		}
		final String value = Strings.nullToEmpty(annotation.value());
		for (int i = 1; i <= parameters.length; ++i) {
			if (!value.contains("$" + i)) { //$NON-NLS-1$
				fail("@Inline value does not contains the string \"$" + i + "\""); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}

}
