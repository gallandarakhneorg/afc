/* 
 * $Id$
 * 
 * Copyright (C) 2010-2012 Stephane GALLAND.
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
package org.arakhne.afc.math.continous.object2d;

import java.util.Comparator;

/**
 * Comparator of Tuple2f.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see {@link org.arakhne.afc.math.geometry.d2.continuous.Tuple2fComparator}
 */
@Deprecated
public class Tuple2fComparator implements Comparator<Tuple2f<?>> {
	
	/**
	 */
	public Tuple2fComparator() {
		//
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare(Tuple2f<?> o1, Tuple2f<?> o2) {
		if (o1==o2) return 0;
		if (o1==null) return Integer.MIN_VALUE;
		if (o2==null) return Integer.MAX_VALUE;
		int cmp = Double.compare(o1.getX(), o2.getX());
		if (cmp!=0) return cmp;
		return Double.compare(o1.getY(), o2.getY());
	}
		
}