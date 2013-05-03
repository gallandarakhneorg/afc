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

package org.arakhne.vmutil;

import java.util.Comparator;

/**
 * This comparator permits to compare two class objects.
 * 
 * @author $Author: galland$
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
	
	/**
	 */
	protected ClassComparator() {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare(Class<?> o1, Class<?> o2) {
		if (o1==o2) return 0;
		if (o1==null) return Integer.MIN_VALUE;
		if (o2==null) return Integer.MAX_VALUE;
		String n1 = o1.getCanonicalName();
		String n2 = o2.getCanonicalName();
		if (n1==n2) return 0;
		if (n1==null) return Integer.MIN_VALUE;
		if (n2==null) return Integer.MAX_VALUE;
		return n1.compareTo(n2);
	}
	
}
