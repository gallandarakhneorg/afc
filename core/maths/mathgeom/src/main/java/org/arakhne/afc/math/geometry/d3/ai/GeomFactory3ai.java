/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d3.ai;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.GeomFactory3D;
import org.arakhne.afc.math.geometry.d3.PathIterator3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.afp.PathIterator3afp;

/** Factory of geometrical elements.
 *
 * @param <E> the types of the path elements.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface GeomFactory3ai<E extends PathElement3ai, P extends Point3D<? super P, ? super V>,
		V extends Vector3D<? super V, ? super P>, B extends RectangularPrism3ai<?, ?, E, P, V, B>>
		extends GeomFactory3D<V, P> {

	/** Create an empty path with the given winding rule.
	 *
	 * @param rule the rule.
	 * @return the new path.
	 */
	Path3ai<?, ?, E, P, V, B> newPath(PathWindingRule rule);

	/** Create a segment.
	 *
	 * @param x1 the x coordinate of the first point of the segment.
	 * @param y1 the y coordinate of the first point of the segment.
	 * @param z1 the y coordinate of the first point of the segment.
	 * @param x2 the x coordinate of the second point of the segment.
	 * @param y2 the y coordinate of the second point of the segment.
	 * @param z2 the y coordinate of the second point of the segment.
	 * @return the new segment.
	 */
	Segment3ai<?, ?, E, P, V, B> newSegment(int x1, int y1, int z1, int x2, int y2, int z2);

	/** Create an empty bounding box.
	 *
	 * @return the box.
	 */
	B newBox();

	/** Create a bounding box.
	 *
	 * @param x the x coordinate of the lower corner.
	 * @param y the y coordinate of the lower corner.
	 * @param z the z coordinate of the lower corner.
	 * @param width the width of the box.
	 * @param height the height of the box.
	 * @param depth the depth of the box.
	 * @return the box.
	 */
	B newBox(int x, int y, int z, int width, int height, int depth);

	/** Create an empty bounding box.
	 *
	 * @return the box.
	 */
	MultiShape3ai<?, ?, ?, E, P, V, B> newMultiShape();

	/** Create a move-to path element to the given point.
	 *
	 * @param x x coordinate of the target point.
	 * @param y y coordinate of the target point.
	 * @param z z coordinate of the target point.
	 * @return the path element.
	 */
	E newMovePathElement(int x, int y, int z);

	/** Create a line-to path element to the given point.
	 *
	 * @param startX x coordinate of the start point.
	 * @param startY y coordinate of the start point.
	 * @param startZ z coordinate of the start point.
	 * @param targetX x coordinate of the target point.
	 * @param targetY y coordinate of the target point.
	 * @param targetZ z coordinate of the target point.
	 * @return the path element.
	 */
	E newLinePathElement(int startX, int startY, int startZ, int targetX, int targetY, int targetZ);

	/** Create a close path element.
	 *
	 * @param lastPointX x coordinate of the last point on the path
	 * @param lastPointY y coordinate of the last point on the path
	 * @param lastPointZ z coordinate of the last point on the path
	 * @param firstPointX x coordinate of the first point on the path.
	 * @param firstPointY y coordinate of the first point on the path.
	 * @param firstPointZ z coordinate of the first point on the path.
	 * @return the path element.
	 */
	E newClosePathElement(int lastPointX, int lastPointY, int lastPointZ, int firstPointX, int firstPointY, int firstPointZ);

	/** Create a quadratic curve path element to the given point through the given control point.
	 *
	 * @param startX x coordinate of the start point.
	 * @param startY y coordinate of the start point.
	 * @param startZ z coordinate of the start point.
	 * @param controlX x coordinate of the control point.
	 * @param controlY y coordinate of the control  point.
	 * @param controlZ z coordinate of the control  point.
	 * @param targetX x coordinate of the target point.
	 * @param targetY y coordinate of the target point.
	 * @param targetZ z coordinate of the target point.
	 * @return the path element.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
    E newCurvePathElement(int startX, int startY, int startZ, int controlX, int controlY, int controlZ, int targetX, int targetY,
            int targetZ);

	/** Create a curve path element to the given point through the two given control points.
	 *
	 * @param startX x coordinate of the start point.
	 * @param startY y coordinate of the start point.
	 * @param startZ z coordinate of the start point.
	 * @param controlX1 x coordinate of the control point.
	 * @param controlY1 y coordinate of the control point.
	 * @param controlZ1 z coordinate of the control point.
	 * @param controlX2 x coordinate of the control point.
	 * @param controlY2 y coordinate of the control point.
	 * @param controlZ2 z coordinate of the control point.
	 * @param targetX x coordinate of the target point.
	 * @param targetY y coordinate of the target point.
	 * @param targetZ z coordinate of the target point.
	 * @return the path element.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	E newCurvePathElement(int startX, int startY, int startZ, int controlX1, int controlY1, int controlZ1,
			int controlX2, int controlY2, int controlZ2, int targetX, int targetY, int targetZ);


    /** Replies the {@link PathIterator3ai} that is corresponding to the given element.
     *
     * <p>If the given element is already a {@link PathIterator3ai}, returns {@code this}.
     *
     * @param iterator the iterator.
     * @return the iterator.
     */
    default PathIterator3ai<?> convert(PathIterator3D<?> iterator) {
        if (iterator instanceof PathIterator3ai) {
            return (PathIterator3ai<?>) iterator;
        }
        assert iterator instanceof PathIterator3afp;
        return new PathIteratorWrapper(this, (PathIterator3afp<?>) iterator);
    }
}
