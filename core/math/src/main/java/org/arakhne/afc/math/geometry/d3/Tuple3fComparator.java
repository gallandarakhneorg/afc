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

import org.arakhne.afc.math.discrete.object3d.Tuple3iComparator;

/**
 * Comparator of Tuple3D on their floating-point coordinates.
 *
 * <p>For comparisons on the integer coordinates, see {@link Tuple3iComparator}.
 * 
 * @author $Author: tpiotrow$
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Tuple3fComparator implements Comparator<Tuple3D<?>> {

	/**
	 */
	public Tuple3fComparator() {
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
		int cmpX = Double.compare(tuple1.getX(), tuple2.getX());
		if (cmpX!=0) {
			return cmpX;
		}
		int cmpY = Double.compare(tuple1.getY(), tuple2.getY());
		if (cmpY!=0) {
			return cmpY;
		}
		return Double.compare(tuple1.getZ(), tuple2.getZ());
	}

}
