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

package org.arakhne.afc.math.geometry.d3.afp;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.GeomFactory3D;
import org.arakhne.afc.math.geometry.d3.PathIterator3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.ai.PathIterator3ai;

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
 */
public interface GeomFactory3afp<E extends PathElement3afp, P extends Point3D<? super P, ? super V>,
		V extends Vector3D<? super V, ? super P>, B extends RectangularPrism3afp<?, ?, E, P, V, B>>
		extends GeomFactory3D<V, P> {

	/** Create an empty path with the given winding rule.
	 *
	 * @param rule the rule.
	 * @return the new path.
	 */
	Path3afp<?, ?, E, P, V, B> newPath(PathWindingRule rule);

	/** Create an empty multishape.
	 *
	 * @return the new multishape.
	 */
	MultiShape3afp<?, ?, ?, E, P, V, B> newMultiShape();

	/** Create an empty bounding box.
	 *
	 * @return the box.
	 */
	B newBox();

	/** Create a bounding box.
	 *
	 * @param x the x coordinate of the front lower corner.
	 * @param y the y coordinate of the front lower corner.
	 * @param z the z coordinate of the front lower corner.
	 * @param width the width of the box.
	 * @param height the height of the box.
	 * @param depth the depth of the box.
	 * @return the box.
	 */
	B newBox(double x, double y, double z, double width, double height, double depth);

	/** Create a move-to path element to the given point.
	 *
	 * @param x x coordinate of the target point.
	 * @param y y coordinate of the target point.
	 * @param z z coordinate of the target point.
	 * @return the path element.
	 */
	E newMovePathElement(double x, double y, double z);

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
	E newLinePathElement(double startX, double startY, double startZ, double targetX, double targetY, double targetZ);

	/** Create a close path element.
	 *
	 * @param lastPointX x coordinate of the last point on the path.
	 * @param lastPointY y coordinate of the last point on the path.
	 * @param lastPointZ z coordinate of the last point on the path.
	 * @param firstPointX x coordinate of the first point on the path.
	 * @param firstPointY y coordinate of the first point on the path.
	 * @param firstPointZ z coordinate of the first point on the path.
	 * @return the path element.
	 */
	E newClosePathElement(double lastPointX, double lastPointY, double lastPointZ, double firstPointX, double firstPointY,
            double firstPointZ);

    /** Create a quadratic curve path element to the given point through the given control point.
     *
     * @param startX x coordinate of the start point.
     * @param startY y coordinate of the start point.
     * @param startZ z coordinate of the start point.
     * @param controlX x coordinate of the control point.
     * @param controlY y coordinate of the control point.
     * @param controlZ z coordinate of the control point.
     * @param targetX x coordinate of the target point.
     * @param targetY y coordinate of the target point.
     * @param targetZ z coordinate of the target point.
     * @return the path element.
     */
    @SuppressWarnings("checkstyle:parameternumber")
    E newCurvePathElement(double startX, double startY, double startZ, double controlX, double controlY, double controlZ,
            double targetX, double targetY, double targetZ);

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
    E newCurvePathElement(double startX, double startY, double startZ, double controlX1, double controlY1, double controlZ1,
            double controlX2, double controlY2, double controlZ2, double targetX, double targetY, double targetZ);


	/** Create a segment.
	 *
	 * @param x1 the x coordinate of the first point of the segment.
	 * @param y1 the y coordinate of the first point of the segment.
	 * @param z1 the z coordinate of the first point of the segment.
	 * @param x2 the x coordinate of the second point of the segment.
	 * @param y2 the y coordinate of the second point of the segment.
	 * @param z2 the z coordinate of the second point of the segment.
	 * @return the new segment.
	 */
	Segment3afp<?, ?, E, P, V, B> newSegment(double x1, double y1, double z1, double x2, double y2, double z2);

    /** Replies the {@link PathIterator3afp} that is corresponding to the given element.
     *
     * <p>If the given element is already a {@link PathIterator3afp}, returns {@code this}.
     *
     * @param iterator the iterator.
     * @return the iterator.
     */
    default PathIterator3afp<?> convert(PathIterator3D<?> iterator) {
        if (iterator instanceof PathIterator3afp) {
            return (PathIterator3afp<?>) iterator;
        }
        assert iterator instanceof PathIterator3ai;
        return new PathIteratorWrapper(this, (PathIterator3ai<?>) iterator);
    }
}
