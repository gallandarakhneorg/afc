/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d3;

import java.util.Comparator;

/**
 * Comparator of Tuple3D on their integer coordinates.
 *
 * <p>For comparisons on the floating-point coordinates, see {@link Tuple3fComparator}.
 * @author $Author: tpiotrow$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Tuple3iComparator implements Comparator<Tuple3D<?>> {

    /** Construct a comparator.
     */
    public Tuple3iComparator() {
        //
    }

	@Override
	public int compare(Tuple3D<?> tuple1, Tuple3D<?> tuple2) {
        if (tuple1 == tuple2) {
            return 0;
        }
        if (tuple1 == null) {
            return Integer.MIN_VALUE;
        }
        if (tuple2 == null) {
            return Integer.MAX_VALUE;
        }
        final int cmpX = tuple1.ix() - tuple2.ix();
        if (cmpX != 0) {
            return cmpX;
        }
        final int cmpY = tuple1.iy() - tuple2.iy();
        if (cmpY != 0) {
            return cmpY;
        }
        return tuple1.iz() - tuple2.iz();
	}

}
