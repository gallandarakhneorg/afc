/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d1.afp;

import org.arakhne.afc.math.geometry.d1.GeomFactory1D;
import org.arakhne.afc.math.geometry.d1.Point1D;
import org.arakhne.afc.math.geometry.d1.Segment1D;
import org.arakhne.afc.math.geometry.d1.Vector1D;

/** Factory of geometrical elements.
 *
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <S> is the type of the segments.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public interface GeomFactory1afp<
		P extends Point1D<? super P, ? super V, ? super S>,
        V extends Vector1D<? super V, ? super P, ? super S>,
        S extends Segment1D<?, ?>,
        B extends Rectangle1afp<?, ?, P, V, S, B>>
        extends GeomFactory1D<V, P> {

    /** Create an empty bounding box.
     *
     * @param segment the segment.
     * @return the box.
     */
    B newBox(S segment);

    /** Create a bounding box.
     *
     * @param segment the segment.
     * @param x the x coordinate of the lower corner.
     * @param y the y coordinate of the lower corner.
     * @param width the width of the box.
     * @param height the height of the box.
     * @return the box.
     */
    B newBox(S segment, double x, double y, double width, double height);

}
