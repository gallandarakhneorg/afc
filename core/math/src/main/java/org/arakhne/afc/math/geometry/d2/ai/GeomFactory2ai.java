/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Stephane GALLAND.
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
package org.arakhne.afc.math.geometry.d2.ai;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.GeomFactory;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;

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
public interface GeomFactory2ai<E extends PathElement2ai, P extends Point2D<? super P, ? super V>,
		V extends Vector2D<? super V, ? super P>, B extends Rectangle2ai<?, ?, E, P, V, B>>
		extends GeomFactory<V, P> {

	/** Create an empty path with the given winding rule.
	 *
	 * @param rule the rule.
	 * @return the new path.
	 */
	Path2ai<?, ?, E, P, V, B> newPath(PathWindingRule rule);
	
	/** Create a segment.
	 *
	 * @param x1 the x coordinate of the first point of the segment.
	 * @param y1 the y coordinate of the first point of the segment.
	 * @param x2 the x coordinate of the second point of the segment.
	 * @param y2 the y coordinate of the second point of the segment.
	 * @return the new segment.
	 */
	Segment2ai<?, ?, E, P, V, B> newSegment(int x1, int y1, int x2, int y2);
	
	/** Create an empty bounding box.
	 *
	 * @return the box.
	 */
	B newBox();

	/** Create a bounding box.
	 *
	 * @param x the x coordinate of the lower corner.
	 * @param y the y coordinate of the lower corner.
	 * @param width the width of the box.
	 * @param height the height of the box.
	 * @return the box.
	 */
	B newBox(int x, int y, int width, int height);

	/** Create an empty bounding box.
	 *
	 * @return the box.
	 */
	MultiShape2ai<?, ?, ?, E, P, V, B> newMultiShape();

	/** Create a move-to path element to the given point.
	 * 
	 * @param x x coordinate of the target point.
	 * @param y y coordinate of the target point.
	 * @return the path element.
	 */
	E newMovePathElement(int x, int y);
	
	/** Create a line-to path element to the given point.
	 * 
	 * @param startX x coordinate of the start point.
	 * @param startY y coordinate of the start point.
	 * @param targetX x coordinate of the target point.
	 * @param targetY y coordinate of the target point.
	 * @return the path element.
	 */
	E newLinePathElement(int startX, int startY, int targetX, int targetY);

	/** Create a close path element.
	 * 
	 * @param lastPointX x coordinate of the last point on the path
	 * @param lastPointy y coordinate of the last point on the path
	 * @param firstPointX x coordinate of the first point on the path.
	 * @param firstPointY y coordinate of the first point on the path.
	 * @return the path element.
	 */
	E newClosePathElement(int lastPointX, int lastPointy, int firstPointX, int firstPointY);

	/** Create a quadratic curve path element to the given point through the given control point.
	 * 
	 * @param startX x coordinate of the start point.
	 * @param startY y coordinate of the start point.
	 * @param controlX x coordinate of the control point.
	 * @param controlY y coordinate of the control  point.
	 * @param targetX x coordinate of the target point.
	 * @param targetY y coordinate of the target point.
	 * @return the path element.
	 */
	E newCurvePathElement(int startX, int startY, int controlX, int controlY, int targetX, int targetY);

	/** Create a curve path element to the given point through the two given control points.
	 * 
	 * @param startX x coordinate of the start point.
	 * @param startY y coordinate of the start point.
	 * @param controlX1 x coordinate of the control point.
	 * @param controlY1 y coordinate of the control  point.
	 * @param controlX2 x coordinate of the control point.
	 * @param controlY2 y coordinate of the control  point.
	 * @param targetX x coordinate of the target point.
	 * @param targetY y coordinate of the target point.
	 * @return the path element.
	 */
	E newCurvePathElement(int startX, int startY, int controlX1, int controlY1,
			int controlX2, int controlY2, int targetX, int targetY);

}