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
package org.arakhne.afc.math.geometry.d3.continuous;

import java.util.Comparator;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Comparator of Tuple2f.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Tuple3fComparator implements Comparator<Tuple3f<?>> {
	
	/**
	 */
	public Tuple3fComparator() {
		//
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Pure
	@Override
	public int compare(Tuple3f<?> o1, Tuple3f<?> o2) {
		if (o1==o2) return 0;
		if (o1==null) return Integer.MIN_VALUE;
		if (o2==null) return Integer.MAX_VALUE;
		int cmp = Double.compare(o1.getX(), o2.getX());
		if (cmp!=0) return cmp;
		cmp = Double.compare(o1.getY(), o2.getY());
		if (cmp!=0) return cmp;
		return Double.compare(o1.getZ(), o2.getZ());
	}
		
}