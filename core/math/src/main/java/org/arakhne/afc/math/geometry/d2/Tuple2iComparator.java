/* 
 * $Id$
 * 
 * Copyright (C) 2011 Janus Core Developers
 * Copyright (C) 2012 Stephane GALLAND.
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
package org.arakhne.afc.math.geometry.d2;

import java.util.Comparator;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Comparator of Tuple2D on their integer coordinates.
 *
 * <p>For comparisons on the floating-point coordinates, see {@link Tuple2fComparator}.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see Tuple2fComparator
 * @since 13.0
 */
public class Tuple2iComparator implements Comparator<Tuple2D<?>> {
	
	/**
	 */
	public Tuple2iComparator() {
		//
	}
	
	@Pure
	@Override
	public int compare(Tuple2D<?> o1, Tuple2D<?> o2) {
		if (o1==o2) {
			return 0;
		}
		if (o1==null) {
			return Integer.MIN_VALUE;
		}
		if (o2==null) {
			return Integer.MAX_VALUE;
		}
		int cmp = o1.ix() - o2.ix();
		if (cmp!=0) {
			return cmp;
		}
		return o1.iy() - o2.iy();
	}
		
}