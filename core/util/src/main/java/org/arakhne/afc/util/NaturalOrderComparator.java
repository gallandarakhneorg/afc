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

import java.util.Comparator;

/** Comparator which is using the natural order of the elements, i.e.
 * the elements must implements {@link Comparable}
 * 
 * @param <OBJ> is the type of the objects to compare to.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class NaturalOrderComparator<OBJ> implements Comparator<OBJ> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int compare(OBJ o1, OBJ o2) {
		if (o1==o2) return 0;
		if (o1==null) return Integer.MIN_VALUE;
		if (o2==null) return Integer.MAX_VALUE;
		try {
			return ((Comparable)o1).compareTo(o2);
		}
		catch(Throwable exception) {
			//
		}
		try {
			return -((Comparable)o2).compareTo(o1);
		}
		catch(Throwable exception) {
			//
		}
		throw new UnsupportedNaturalOrderException(o1);
	}

}
