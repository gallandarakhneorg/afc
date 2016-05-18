/* 
 * $Id$
 * 
 * Copyright (c) 2016, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Laboratoire Systemes et Transports (SET)
 * of Universite de Technologie de Belfort-Montbeliard.
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SET.
 *
 * http://www.multiagent.fr/
 */
package org.arakhne.afc.math.geometry.d3;

import java.util.Comparator;

/**
 * Comparator of Tuple3D on their integer coordinates.
 *
 * <p>For comparisons on the floating-point coordinates, see {@link Tuple3fComparator}.
 * @author $Author: tpiotrow$
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Tuple3iComparator implements Comparator<Tuple3D<?>> {
	
	/**
	 */
	public Tuple3iComparator() {
		// 
	}

	@Override
	public int compare(Tuple3D<?> tuple1, Tuple3D<?> tuple2) {
		if (tuple1==tuple2) {
			return 0;
		}
		if (tuple1==null) {
			return Integer.MIN_VALUE;
		}
		if (tuple2==null) {
			return Integer.MAX_VALUE;
		}
		int cmpX = tuple1.ix() - tuple2.ix();
		if (cmpX!=0) {
			return cmpX;
		}
		int cmpY = tuple1.iy() - tuple2.iy();
		if (cmpY!=0) {
			return cmpY;
		}
		return tuple1.iz() - tuple2.iz();
	}

}
