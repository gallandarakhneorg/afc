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

package org.arakhne.afc.vmutil;

import java.util.Comparator;

/**
 * This comparator permits to compare two objects according to there references.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 11.0
 */
public class ObjectReferenceComparator implements Comparator<Object> {

	/**
	 * Singleton of a class comparator.
	 */
	public static final ObjectReferenceComparator SINGLETON = new ObjectReferenceComparator();
	
	/**
	 * @see #SINGLETON
	 */
	protected ObjectReferenceComparator() {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare(Object o1, Object o2) {
		if (o1==o2) return 0;
		if (o1==null) return Integer.MIN_VALUE;
		if (o2==null) return Integer.MAX_VALUE;
		int ref1 = System.identityHashCode(o1);
		int ref2 = System.identityHashCode(o2);
		return ref1 - ref2;
	}
	
}
