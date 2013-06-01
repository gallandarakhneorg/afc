/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Christophe BOHRHAUER.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
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
package org.arakhne.afc.math.transform;

import java.util.List;

import org.arakhne.afc.math.object.Point1D;
import org.arakhne.afc.math.object.Segment1D;


/** This interface representes an objects on which
 * geometrical transformations could be applied.
 * <p>
 * If a path was given, this transformation uses this list of the segments to translate
 * a point and change its associated segment if required. If no path was given to
 * this transformation, all the translation will be located on the current segment
 * space (even if the coordinates are too big or too low to be on the segment).
 * The transformed entities (<code>Point1D</code>...) is supposed to be on
 * the segments of the path if it was supplied. The path is the list of the segments
 * which must follow the current segment of the entity.
 * <p>
 * Caution, the stored path is the reference passed to the functions, not a copy.
 * <p>
 * If a path was given, the translation is positive along the path. If a path
 * was not given, the translation will be directly applied to the coordinates. 
 *
 * @param <S> is the type of segment on which the transformable object is located.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Transformable1D<S extends Segment1D> {

    /**
     * Set the transformation matrix to the identity.
     */
    public void setIdentityTransform();

    /**
     * Set the transformation matrix of the object.
     * 
     * @param trans is the transformation matrix
     */
    public void setTransform(Transform1D<S> trans);

    /**
     * Transforms the Bounds object by the given transform.
     * 
     * @param trans is the transformation matrix
     */
    public void transform(Transform1D<S> trans);
    
    /**
     * Replies the transformation matrix.
     * 
     * @return the transformation matrix
     */
    public Transform1D<S> getTransformMatrix();

    /**
     * Set the position of the object.
     * The jutting coordinate is unchanged.
     * 
     * @param curvilineCoord is the curviline position.
     */
    public void setTranslation(float curvilineCoord);

    /**
     * Set the position of the object.
     * 
     * @param position is the position
     */
    public void setTranslation(Point1D position);

    /**
     * Replies the position of the object.
     * 
     * @return the position
     */
    public Point1D getTranslation();

    /**
     * Translate the object.
     * The jutting coordinate is unchanged.
     * 
     * @param curvilineMove is the curviline translation.
     */
    public void translate(float curvilineMove);

    /**
     * Translate the object.
     * The jutting coordinate is unchanged.
     *
     * @param path is the path to follow.
     * @param curvilineMove is the curviline translation. This value must be positive
     * to follow the path.
     */
    public void translate(List<? extends S> path, float curvilineMove);

}