/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

package org.arakhne.afc.vmutil;

import java.util.Comparator;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This comparator permits to compare two class objects.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 7.0
 */
public class ClassComparator implements Comparator<Class<?>> {

	/**
	 * Singleton of a class comparator.
	 */
	public static final ClassComparator SINGLETON = new ClassComparator();

	/** Construct a comparator.
	 *
	 * @see #SINGLETON
	 */
	protected ClassComparator() {
		//
	}

	@Pure
	@Override
	public int compare(Class<?> o1, Class<?> o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return Integer.MIN_VALUE;
		}
		if (o2 == null) {
			return Integer.MAX_VALUE;
		}
		final String n1 = o1.getCanonicalName();
		final String n2 = o2.getCanonicalName();
		if (n1 == n2) {
			return 0;
		}
		if (n1 == null) {
			return Integer.MIN_VALUE;
		}
		if (n2 == null) {
			return Integer.MAX_VALUE;
		}
		return n1.compareTo(n2);
	}

}
