/* 
 * $Id$
 * 
 * Copyright (C) 2005-09 Stephane GALLAND.
 * Copyright (C) 2012-13 Stephane GALLAND.
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

package org.arakhne.afc.ui;

import org.arakhne.afc.math.continous.object2d.Circle2f;
import org.arakhne.afc.math.continous.object2d.Ellipse2f;
import org.arakhne.afc.math.continous.object2d.PathIterator2f;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.math.continous.object2d.RoundRectangle2f;
import org.arakhne.afc.math.continous.object2d.Segment2f;
import org.arakhne.afc.math.continous.object2d.Shape2f;

/** This interface describes a zooming context and
 * permits to make some operation in it.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public interface ZoomableContext {

	/** Translates the specified workspace length
	 *  into the screen length.
	 *
	 * @param l is the length in the workspace space.
	 * @return a length into the screen space.
	 */
	public float logical2pixel_size(float l);
	
	/** Translates the specified workspace location
	 *  into the screen location.
	 *
	 * @param l is the coordinate along the workspace space X-axis.
	 * @return a location along the screen space X-axis.
	 */
	public float logical2pixel_x(float l);
	
	/** Translates the specified workspace location
	 *  into the screen location.
	 *
	 * @param l is the coordinate along the workspace space Y-axis.
	 * @return a location along the screen space Y-axis.
	 */
	public float logical2pixel_y(float l);
	
	/** Translates the specified screen length
	 *  into the logical length.
	 *
	 * @param l is the length in the screen space.
	 * @return a length into the logical space.
	 */
	public float pixel2logical_size(float l);
	
	/** Translates the specified screen location
	 *  into the logical location.
	 *
	 * @param l is the location along the screen space X-axis.
	 * @return a location along the logical space X-axis.
	 */
	public float pixel2logical_x(float l);
	
	/** Translates the specified screen location
	 *  into the logical location.
	 *
	 * @param l is the location along the screen space Y-axis.
	 * @return a location along the logical space Y-axis.
	 */
	public float pixel2logical_y(float l);

	/** Translates the specified path
	 *  into the screen space.
	 *
	 * @param p is the path in the logical.
	 * @return the path is screen path.
	 */
	public PathIterator2f logical2pixel(PathIterator2f p);

	/** Translates the specified path
	 *  into the logical space.
	 *
	 * @param p is the path in the screen space.
	 * @return the path in logical space.
	 */
	public PathIterator2f pixel2logical(PathIterator2f p);

	/** Translates the specified segment
	 *  into the screen space.
	 *
	 * @param s is the segment in the logical space when input and the
	 * same segment in screen space when output.
	 */
	public void logical2pixel(Segment2f s);

	/** Translates the specified segment
	 *  into the logical space.
	 *
	 * @param s is the segment in the screen space when input and the
	 * same segment in logical space when output.
	 */
	public void pixel2logical(Segment2f s);

	/** Translates the specified rectangle
	 *  into the screen space.
	 *
	 * @param r is the rectangle in the logical space when input and the
	 * same rectangle in screen space when output.
	 */
	public void logical2pixel(RoundRectangle2f r);

	/** Translates the specified rectangle
	 *  into the logical space.
	 *
	 * @param r is the rectangle in the screen space when input and the
	 * same rectangle in logical space when output.
	 */
	public void pixel2logical(RoundRectangle2f r);

	/** Translates the specified point
	 *  into the screen space.
	 *
	 * @param p is the point in the logical space when input and the
	 * same point in screen space when output.
	 */
	public void logical2pixel(Point2f p);

	/** Translates the specified point
	 *  into the logical space.
	 *
	 * @param p is the point in the screen space when input and the
	 * same point in logical space when output.
	 */
	public void pixel2logical(Point2f p);

	/** Translates the specified ellipse
	 *  into the screen space.
	 *
	 * @param e is the ellipse in the logical space when input and the
	 * same ellipse in screen space when output.
	 */
	public void logical2pixel(Ellipse2f e);

	/** Translates the specified ellipse
	 *  into the logical space.
	 *
	 * @param e is the ellipse in the screen space when input and the
	 * same ellipse in logical space when output.
	 */
	public void pixel2logical(Ellipse2f e);

	/** Translates the specified circle
	 *  into the screen space.
	 *
	 * @param r is the rectangle in the logical space when input and the
	 * same rectangle in screen space when output.
	 */
	public void logical2pixel(Circle2f r);

	/** Translates the specified circle
	 *  into the logical space.
	 *
	 * @param r is the rectangle in the screen space when input and the
	 * same rectangle in logical space when output.
	 */
	public void pixel2logical(Circle2f r);

	/** Translates the specified rectangle
	 *  into the screen space.
	 *
	 * @param r is the rectangle in the logical space when input and the
	 * same rectangle in screen space when output.
	 */
	public void logical2pixel(Rectangle2f r);

	/** Translates the specified rectangle
	 *  into the logical space.
	 *
	 * @param r is the rectangle in the screen space when input and the
	 * same rectangle in logical space when output.
	 */
	public void pixel2logical(Rectangle2f r);

	/** Translates the specified shape
	 *  into the screen space.
	 *  <p>
	 *  <strong>CAUTION:</strong> You must prefer to invoke 
	 *  one of the other functions of this ZoomableContext
	 *  to run a faster translation algorithm than the
	 *  algorithm implemented in this function. 
	 *
	 * @param s is the shape in the logical space when input and the
	 * same shape in screen space when output.
	 * @return the translated shape.
	 */
	public Shape2f logical2pixel(Shape2f s);

	/** Translates the specified shape
	 *  into the logical space.
	 *  <p>
	 *  <strong>CAUTION:</strong> You must prefer to invoke 
	 *  one of the other functions of this ZoomableContext
	 *  to run a faster translation algorithm than the
	 *  algorithm implemented in this function. 
	 *
	 * @param s is the shape in the screen space when input and the
	 * same shape in logical space when output.
	 * @return the translated shape.
	 */
	public Shape2f pixel2logical(Shape2f s);

	/** Replies the graphical zoom factor.
     * 
     * @return the scale factor.
     */
    public float getScalingFactor();    

	/** Replies the maximal scaling factor allowing in the view
	 * 
	 * @return the maximal scaling factor.
	 */
	public float getMaxScalingFactor();

	/** Replies the minimal scaling factor allowing in the view
	 * 
	 * @return the minimal scaling factor.
	 */
	public float getMinScalingFactor();
	
	/** Replies the sensitivity of the {@code zoomIn()}
	 * and {@code zoomOut()} functions.
	 * 
	 * @return the sensitivity of the zooming functions.
	 */
	public float getScalingSensitivity();
	
	/** Replies the X coordinate of the point that is drawn
	 * at the center of the graphical viewport.
	 * 
	 * @return the X coordinate of the view center. 
	 */
	public float getFocusX();

	/** Replies the Y coordinate of the point that is drawn
	 * at the center of the graphical viewport.
	 * 
	 * @return the Y coordinate of the view center. 
	 */
	public float getFocusY();
	
	/** Replies if the X axis is inverted (positives are to the left)
	 * than the standard UI X axis (positives are to the right).
	 * 
	 * @return <code>true</code> if the positives are to the left;
	 * <code>false</code> if the positives are to the right. 
	 */
	public boolean isXAxisInverted();

	/** Replies if the Y axis is inverted (positives are to the top)
	 * than the standard UI Y axis (positives are to the bottom).
	 * 
	 * @return <code>true</code> if the positives are to the top;
	 * <code>false</code> if the positives are to the bottom. 
	 */
	public boolean isYAxisInverted();

}