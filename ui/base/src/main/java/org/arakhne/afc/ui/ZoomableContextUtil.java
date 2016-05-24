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

package org.arakhne.afc.ui;

import org.arakhne.afc.math.continous.object2d.Circle2f;
import org.arakhne.afc.math.continous.object2d.Ellipse2f;
import org.arakhne.afc.math.continous.object2d.Path2f;
import org.arakhne.afc.math.continous.object2d.PathElement2f;
import org.arakhne.afc.math.continous.object2d.PathIterator2f;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.math.continous.object2d.RoundRectangle2f;
import org.arakhne.afc.math.continous.object2d.Segment2f;
import org.arakhne.afc.math.continous.object2d.Shape2f;
import org.arakhne.afc.math.generic.PathWindingRule;

/** Utilities for ZoomableContext.
*
* @author $Author: sgalland$
* @version $FullVersion$
* @mavengroupid $GroupId$
* @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public class ZoomableContextUtil {

	/** Replies the scaling factor that permits to pass
	 * from the given size expressed in the logical space to
	 * the given size expressed in the pixel space.
	 * 
	 * @param sizeInLogicalSpace is the size in the logical space. Must not be strictly positive.
	 * @param sizeInPixelSpace is the size in the screen/pixel space. Must not be strictly positive.
	 * @return the scaling factor to pass from the logical size to the pixel size.
	 */
	public static float determineFactor(float sizeInLogicalSpace, float sizeInPixelSpace) {
		assert(sizeInLogicalSpace>0f) : "sizeInLogicalSpace is not strictly positive"; //$NON-NLS-1$
		assert(sizeInPixelSpace>0f) : "sizeInPixelSpace is not strictly positive"; //$NON-NLS-1$
		return sizeInPixelSpace / sizeInLogicalSpace;
	}
	
	/** Translates the specified path
	 *  into the screen space.
	 *
	 * @param p is the path in the logical.
	 * @param centeringTransform is the transform to apply to the points to change from/to the coordinate
	 * system from the "global" logical coordinate system to/from the "centered" logical coordinate
	 * system.
	 * @param zoom is the current zooming factor of the view.
	 * @return the path is screen path.
	 */
	public static PathIterator2f logical2pixel(PathIterator2f p,
			CenteringTransform centeringTransform,
			float zoom) {
		return new L2PPathIterator(p, centeringTransform, zoom);
	}

	/** Translates the specified path
	 *  into the logical space.
	 *
	 * @param p is the path in the screen space.
	 * @param centeringTransform is the transform to apply to the points to change from/to the coordinate
	 * system from the "global" logical coordinate system to/from the "centered" logical coordinate
	 * system.
	 * @param zoom is the current zooming factor of the view.
	 * @return the path in logical space.
	 */
	public static PathIterator2f pixel2logical(PathIterator2f p,
			CenteringTransform centeringTransform,
			float zoom) {
		return new P2LPathIterator(p, centeringTransform, zoom);
	}

	/** Translates the specified segment
	 *  into the screen space.
	 *
	 * @param s is the segment in the logical space when input and the
	 * same segment in screen space when output.
	 * @param centeringTransform is the transform to apply to the points to change from/to the coordinate
	 * system from the "global" logical coordinate system to/from the "centered" logical coordinate
	 * system.
	 * @param zoom is the current zooming factor of the view.
	 */
	public static void logical2pixel(Segment2f s,
			CenteringTransform centeringTransform,
			float zoom) {
		assert(s!=null);
		assert(centeringTransform!=null);
		s.set(
				logical2pixel_x(s.getX1(), centeringTransform, zoom),
				logical2pixel_y(s.getY1(), centeringTransform, zoom),
				logical2pixel_x(s.getX2(), centeringTransform, zoom),
				logical2pixel_y(s.getY2(), centeringTransform, zoom));
	}

	/** Translates the specified segment
	 *  into the logical space.
	 *
	 * @param s is the segment in the screen space when input and the
	 * same segment in logical space when output.
	 * @param centeringTransform is the transform to apply to the points to change from/to the coordinate
	 * system from the "global" logical coordinate system to/from the "centered" logical coordinate
	 * system.
	 * @param zoom is the current zooming factor of the view.
	 */
	public static void pixel2logical(Segment2f s,
			CenteringTransform centeringTransform,
			float zoom) {
		assert(s!=null);
		assert(centeringTransform!=null);
		s.set(
				pixel2logical_x(s.getX1(), centeringTransform, zoom),
				pixel2logical_y(s.getX1(), centeringTransform, zoom),
				pixel2logical_x(s.getX2(), centeringTransform, zoom),
				pixel2logical_y(s.getX2(), centeringTransform, zoom));
	}

	/** Translates the specified rectangle
	 *  into the screen space.
	 *
	 * @param r is the rectangle in the logical space when input and the
	 * same rectangle in screen space when output.
	 * @param centeringTransform is the transform to apply to the points to change from/to the coordinate
	 * system from the "global" logical coordinate system to/from the "centered" logical coordinate
	 * system.
	 * @param zoom is the current zooming factor of the view.
	 */
	public static void logical2pixel(RoundRectangle2f r,
			CenteringTransform centeringTransform,
			float zoom) {
		assert(r!=null);
		assert(centeringTransform!=null);
		float x = centeringTransform.isXAxisFlipped() ? r.getMaxX() : r.getMinX();
		float y = centeringTransform.isYAxisFlipped() ? r.getMaxY() : r.getMinY();
		r.set(
				logical2pixel_x(x, centeringTransform, zoom),
				logical2pixel_y(y, centeringTransform, zoom),
				logical2pixel_size(r.getWidth(), zoom),
				logical2pixel_size(r.getHeight(), zoom),
				logical2pixel_size(r.getArcWidth(), zoom),
				logical2pixel_size(r.getArcHeight(), zoom));
	}

	/** Translates the specified rectangle
	 *  into the logical space.
	 *
	 * @param r is the rectangle in the screen space when input and the
	 * same rectangle in logical space when output.
	 * @param centeringTransform is the transform to apply to the points to change from/to the coordinate
	 * system from the "global" logical coordinate system to/from the "centered" logical coordinate
	 * system.
	 * @param zoom is the current zooming factor of the view.
	 */
	public static void pixel2logical(RoundRectangle2f r,
			CenteringTransform centeringTransform,
			float zoom) {
		assert(r!=null);
		assert(centeringTransform!=null);
		float x = centeringTransform.isXAxisFlipped() ? r.getMaxX() : r.getMinX();
		float y = centeringTransform.isYAxisFlipped() ? r.getMaxY() : r.getMinY();
		r.set(
				pixel2logical_x(x, centeringTransform, zoom),
				pixel2logical_y(y, centeringTransform, zoom),
				pixel2logical_size(r.getWidth(), zoom),
				pixel2logical_size(r.getHeight(), zoom),
				pixel2logical_size(r.getArcWidth(), zoom),
				pixel2logical_size(r.getArcHeight(), zoom));
	}

	/** Translates the specified point
	 *  into the screen space.
	 *
	 * @param p is the point in the logical space when input and the
	 * same point in screen space when output.
	 * @param centeringTransform is the transform to apply to the points to change from/to the coordinate
	 * system from the "global" logical coordinate system to/from the "centered" logical coordinate
	 * system.
	 * @param zoom is the current zooming factor of the view.
	 */
	public static void logical2pixel(Point2f p,
			CenteringTransform centeringTransform,
			float zoom) {
		assert(p!=null);
		assert(centeringTransform!=null);
		p.set(
				logical2pixel_x(p.getX(), centeringTransform, zoom),
				logical2pixel_y(p.getY(), centeringTransform, zoom));
	}

	/** Translates the specified point
	 *  into the logical space.
	 *
	 * @param p is the point in the screen space when input and the
	 * same point in logical space when output.
	 * @param centeringTransform is the transform to apply to the points to change from/to the coordinate
	 * system from the "global" logical coordinate system to/from the "centered" logical coordinate
	 * system.
	 * @param zoom is the current zooming factor of the view.
	 */
	public static void pixel2logical(Point2f p,
			CenteringTransform centeringTransform,
			float zoom) {
		assert(p!=null);
		assert(centeringTransform!=null);
		p.set(
				pixel2logical_x(p.getX(), centeringTransform, zoom),
				pixel2logical_y(p.getY(), centeringTransform, zoom));
	}

	/** Translates the specified ellipse
	 *  into the screen space.
	 *
	 * @param e is the ellipse in the logical space when input and the
	 * same ellipse in screen space when output.
	 * @param centeringTransform is the transform to apply to the points to change from/to the coordinate
	 * system from the "global" logical coordinate system to/from the "centered" logical coordinate
	 * system.
	 * @param zoom is the current zooming factor of the view.
	 */
	public static void logical2pixel(Ellipse2f e,
			CenteringTransform centeringTransform,
			float zoom) {
		assert(e!=null);
		assert(centeringTransform!=null);
		float x = centeringTransform.isXAxisFlipped() ? e.getMaxX() : e.getMinX();
		float y = centeringTransform.isYAxisFlipped() ? e.getMaxY() : e.getMinY();
		e.set(
				logical2pixel_x(x, centeringTransform, zoom),
				logical2pixel_y(y, centeringTransform, zoom),
				logical2pixel_size(e.getWidth(), zoom),
				logical2pixel_size(e.getHeight(), zoom));
	}

	/** Translates the specified ellipse
	 *  into the logical space.
	 *
	 * @param e is the ellipse in the screen space when input and the
	 * same ellipse in logical space when output.
	 * @param centeringTransform is the transform to apply to the points to change from/to the coordinate
	 * system from the "global" logical coordinate system to/from the "centered" logical coordinate
	 * system.
	 * @param zoom is the current zooming factor of the view.
	 */
	public static void pixel2logical(Ellipse2f e,
			CenteringTransform centeringTransform,
			float zoom) {
		assert(e!=null);
		assert(centeringTransform!=null);
		float x = centeringTransform.isXAxisFlipped() ? e.getMaxX() : e.getMinX();
		float y = centeringTransform.isYAxisFlipped() ? e.getMaxY() : e.getMinY();
		e.set(
				pixel2logical_x(x, centeringTransform, zoom),
				pixel2logical_y(y, centeringTransform, zoom),
				pixel2logical_size(e.getWidth(), zoom),
				pixel2logical_size(e.getHeight(), zoom));
	}

	/** Translates the specified circle
	 *  into the screen space.
	 *
	 * @param r is the rectangle in the logical space when input and the
	 * same rectangle in screen space when output.
	 * @param centeringTransform is the transform to apply to the points to change from/to the coordinate
	 * system from the "global" logical coordinate system to/from the "centered" logical coordinate
	 * system.
	 * @param zoom is the current zooming factor of the view.
	 */
	public static void logical2pixel(Circle2f r,
			CenteringTransform centeringTransform,
			float zoom) {
		assert(r!=null);
		assert(centeringTransform!=null);
		r.set(
				logical2pixel_x(r.getX(), centeringTransform, zoom),
				logical2pixel_y(r.getY(), centeringTransform, zoom),
				logical2pixel_size(r.getRadius(), zoom));
	}

	/** Translates the specified circle
	 *  into the logical space.
	 *
	 * @param r is the rectangle in the screen space when input and the
	 * same rectangle in logical space when output.
	 * @param centeringTransform is the transform to apply to the points to change from/to the coordinate
	 * system from the "global" logical coordinate system to/from the "centered" logical coordinate
	 * system.
	 * @param zoom is the current zooming factor of the view.
	 */
	public static void pixel2logical(Circle2f r,
			CenteringTransform centeringTransform,
			float zoom) {
		assert(r!=null);
		assert(centeringTransform!=null);
		r.set(
				pixel2logical_x(r.getX(), centeringTransform, zoom),
				pixel2logical_y(r.getY(), centeringTransform, zoom),
				pixel2logical_size(r.getRadius(), zoom));
	}

	/** Translates the specified rectangle
	 *  into the screen space.
	 *
	 * @param r is the rectangle in the logical space when input and the
	 * same rectangle in screen space when output.
	 * @param centeringTransform is the transform to apply to the points to change from/to the coordinate
	 * system from the "global" logical coordinate system to/from the "centered" logical coordinate
	 * system.
	 * @param zoom is the current zooming factor of the view.
	 */
	public static void logical2pixel(Rectangle2f r,
			CenteringTransform centeringTransform,
			float zoom) {
		assert(r!=null);
		assert(centeringTransform!=null);
		float x = centeringTransform.isXAxisFlipped() ? r.getMaxX() : r.getMinX();
		float y = centeringTransform.isYAxisFlipped() ? r.getMaxY() : r.getMinY();
		r.set(
				logical2pixel_x(x, centeringTransform, zoom),
				logical2pixel_y(y, centeringTransform, zoom),
				logical2pixel_size(r.getWidth(), zoom),
				logical2pixel_size(r.getHeight(), zoom));
	}

	/** Translates the specified rectangle
	 *  into the logical space.
	 *
	 * @param r is the rectangle in the screen space when input and the
	 * same rectangle in logical space when output.
	 * @param centeringTransform is the transform to apply to the points to change from/to the coordinate
	 * system from the "global" logical coordinate system to/from the "centered" logical coordinate
	 * system.
	 * @param zoom is the current zooming factor of the view.
	 */
	public static void pixel2logical(Rectangle2f r,
			CenteringTransform centeringTransform,
			float zoom) {
		assert(r!=null);
		assert(centeringTransform!=null);
		float x = centeringTransform.isXAxisFlipped() ? r.getMaxX() : r.getMinX();
		float y = centeringTransform.isYAxisFlipped() ? r.getMaxY() : r.getMinY();
		r.set(
				pixel2logical_x(x, centeringTransform, zoom),
				pixel2logical_y(y, centeringTransform, zoom),
				pixel2logical_size(r.getWidth(), zoom),
				pixel2logical_size(r.getHeight(), zoom));
	}

	/** Translates the specified shape
	 *  into the screen space.
	 *  <p>
	 *  <strong>CAUTION:</strong> You must prefer to invoke 
	 *  one of the other functions of this ZoomableContext
	 *  to run a faster translation algorithm than the
	 *  algorithm implemented in this function. 
	 *
	 * @param s is the shape in the logical space to translate.
	 * @param centeringTransform is the transform to apply to the points to change from/to the coordinate
	 * system from the "global" logical coordinate system to/from the "centered" logical coordinate
	 * system.
	 * @param zoom is the current zooming factor of the view.
	 * @return the result of the translation.
	 */
	public static Shape2f logical2pixel(Shape2f s,
			CenteringTransform centeringTransform,
			float zoom) {
		assert(s!=null);
		assert(centeringTransform!=null);
		if (s instanceof Rectangle2f) {
			Rectangle2f r = ((Rectangle2f)s).clone();
			logical2pixel(r, centeringTransform, zoom);
			return r;
		}
		if (s instanceof Circle2f) {
			Circle2f r = ((Circle2f)s).clone();
			logical2pixel(r, centeringTransform, zoom);
			return r;
		}
		if (s instanceof Ellipse2f) {
			Ellipse2f r = ((Ellipse2f)s).clone();
			logical2pixel(r, centeringTransform, zoom);
			return r;
		}
		if (s instanceof RoundRectangle2f) {
			RoundRectangle2f r = ((RoundRectangle2f)s).clone();
			logical2pixel(r, centeringTransform, zoom);
			return r;
		}
		if (s instanceof Segment2f) {
			Segment2f r = ((Segment2f)s).clone();
			logical2pixel(r, centeringTransform, zoom);
			return r;
		}
		return new Path2f(logical2pixel(s.getPathIterator(), centeringTransform, zoom));
	}

	/** Translates the specified shape
	 *  into the logical space.
	 *  <p>
	 *  <strong>CAUTION:</strong> You must prefer to invoke 
	 *  one of the other functions of this ZoomableContext
	 *  to run a faster translation algorithm than the
	 *  algorithm implemented in this function. 
	 *
	 * @param s is the shape in the screen space to translate.
	 * @param centeringTransform is the transform to apply to the points to change from/to the coordinate
	 * system from the "global" logical coordinate system to/from the "centered" logical coordinate
	 * system.
	 * @param zoom is the current zooming factor of the view.
	 * @return the result of the translation.
	 */
	public static Shape2f pixel2logical(Shape2f s,
			CenteringTransform centeringTransform,
			float zoom) {
		assert(s!=null);
		assert(centeringTransform!=null);
		if (s instanceof Rectangle2f) {
			Rectangle2f r = ((Rectangle2f)s).clone();
			pixel2logical(r, centeringTransform, zoom);
			return r;
		}
		if (s instanceof Circle2f) {
			Circle2f r = ((Circle2f)s).clone();
			pixel2logical(r, centeringTransform, zoom);
			return r;
		}
		if (s instanceof Ellipse2f) {
			Ellipse2f r = ((Ellipse2f)s).clone();
			pixel2logical(r, centeringTransform, zoom);
			return r;
		}
		if (s instanceof RoundRectangle2f) {
			RoundRectangle2f r = ((RoundRectangle2f)s).clone();
			pixel2logical(r, centeringTransform, zoom);
			return r;
		}
		if (s instanceof Segment2f) {
			Segment2f r = ((Segment2f)s).clone();
			pixel2logical(r, centeringTransform, zoom);
			return r;
		}
		return new Path2f(logical2pixel(s.getPathIterator(), centeringTransform, zoom));
	}

	/** Translates the specified workspace location
	 *  into the screen location.
	 *
	 * @param l is the coordinate along the workspace space X-axis.
	 * @param centeringTransform is the transform to apply to the points to change from/to the coordinate
	 * system from the "global" logical coordinate system to/from the "centered" logical coordinate
	 * system.
	 * @param zoom is the current zooming factor of the view.
	 * @return a location along the screen space X-axis.
	 */
	public static float logical2pixel_x(float l, CenteringTransform centeringTransform,
			float zoom) {
		return centeringTransform.toCenterX(l) * zoom;
	}

	/** Translates the specified workspace location
	 *  into the screen location.
	 *
	 * @param l is the coordinate along the workspace space Y-axis.
	 * @param centeringTransform is the transform to apply to the points to change from/to the coordinate
	 * system from the "global" logical coordinate system to/from the "centered" logical coordinate
	 * system.
	 * @param zoom is the current zooming factor of the view.
	 * @return a location along the screen space Y-axis.
	 */
	public static float logical2pixel_y(float l,
			CenteringTransform centeringTransform, float zoom) {
		return centeringTransform.toCenterY(l) * zoom;
	}

	/** Translates the specified screen location
	 *  into the logical location.
	 *
	 * @param p is the location along the screen space X-axis.
	 * @param centeringTransform is the transform to apply to the points to change from/to the coordinate
	 * system from the "global" logical coordinate system to/from the "centered" logical coordinate
	 * system.
	 * @param zoom is the current zooming factor of the view.
	 * @return a location along the logical space X-axis.
	 */
	public static float pixel2logical_x(float p,
			CenteringTransform centeringTransform, float zoom) {
		return centeringTransform.toGlobalX(p/zoom);
	}

	/** Translates the specified screen location
	 *  into the logical location.
	 *
	 * @param p is the location along the screen space Y-axis.
	 * @param centeringTransform is the transform to apply to the points to change from/to the coordinate
	 * system from the "global" logical coordinate system to/from the "centered" logical coordinate
	 * system.
	 * @param zoom is the current zooming factor of the view.
	 * @return a location along the logical space Y-axis.
	 */
	public static float pixel2logical_y(float p, CenteringTransform centeringTransform, float zoom) {
		return centeringTransform.toGlobalY(p/zoom);
	}

	/** Translates the specified workspace length
	 *  into the screen length.
	 *
	 * @param l is the length in the workspace space.
	 * @param zoom is the current zooming factor of the view.
	 * @return a length into the screen space.
	 */
	public static float logical2pixel_size(float l, float zoom) {
		float s = l * zoom;
		if ((l!=0)&&(s==0)) s = 1f;
		return s;
	}

	/** Translates the specified screen length
	 *  into the logical length.
	 *
	 * @param l is the length in the screen space.
	 * @param zoom is the current zooming factor of the view.
	 * @return a length into the logical space.
	 */
	public static float pixel2logical_size(float l, float zoom) {
		return l / zoom;
	}

	
	/** Pixel-to-logical translator for path iterator.
	*
	* @author $Author: sgalland$
	* @version $FullVersion$
	* @mavengroupid $GroupId$
	* @mavenartifactid $ArtifactId$
	*/
	public static class P2LPathIterator implements PathIterator2f {

		private final float[] coords = new float[6];
		private final PathIterator2f it;
		private final CenteringTransform ct;
		private final float z;
		
		/**
		 * @param it is the iterator to translate from pixel to logical coordinate system.
		 * @param ct is the transformation to apply to center the transformation on the view.
		 * @param z is the scaling factor for the translation.
		 */
		public P2LPathIterator(PathIterator2f it, CenteringTransform ct, float z) {
			this.it = it;
			this.ct = ct;
			this.z = z;
		}

		@Override
		public boolean hasNext() {
			return this.it.hasNext();
		}

		@Override
		public PathElement2f next() {
			PathElement2f p = this.it.next();
			p.toArray(this.coords);
			for(int i=0; i<this.coords.length;) {
				this.coords[i] = logical2pixel_x(this.coords[i], this.ct, this.z);
				++i;
				this.coords[i] = logical2pixel_y(this.coords[i], this.ct, this.z);
				++i;
			}
			return PathElement2f.newInstance(
					p.type,
					logical2pixel_x(p.fromX, this.ct, this.z),
					logical2pixel_y(p.fromY, this.ct, this.z),
					this.coords);
		}

		@Override
		public void remove() {
			this.it.remove();
		}

		@Override
		public PathWindingRule getWindingRule() {
			return this.it.getWindingRule();
		}

		@Override
		public boolean isPolyline() {
			return this.it.isPolyline();
		}
				
	}
	
	/** Logical-to-pixel translator for path iterator.
	*
	* @author $Author: sgalland$
	* @version $FullVersion$
	* @mavengroupid $GroupId$
	* @mavenartifactid $ArtifactId$
	*/
	public static class L2PPathIterator implements PathIterator2f {

		private final float[] coords = new float[6];
		private final PathIterator2f it;
		private final CenteringTransform ct;
		private final float z;
		
		/**
		 * @param it is the iterator to translate from logical to pixel coordinate system.
		 * @param ct is the transformation to apply to center the transformation on the view.
		 * @param z is the scaling factor for the translation.
		 */
		public L2PPathIterator(PathIterator2f it, CenteringTransform ct, float z) {
			this.it = it;
			this.ct = ct;
			this.z = z;
		}

		@Override
		public boolean hasNext() {
			return this.it.hasNext();
		}

		@Override
		public PathElement2f next() {
			PathElement2f p = this.it.next();
			p.toArray(this.coords);
			for(int i=0; i<this.coords.length;) {
				this.coords[i] = logical2pixel_x(this.coords[i], this.ct, this.z);
				++i;
				this.coords[i] = logical2pixel_y(this.coords[i], this.ct, this.z);
				++i;
			}
			return PathElement2f.newInstance(
					p.type,
					logical2pixel_x(p.fromX, this.ct, this.z),
					logical2pixel_y(p.fromY, this.ct, this.z),
					this.coords);
		}

		@Override
		public void remove() {
			this.it.remove();
		}

		@Override
		public PathWindingRule getWindingRule() {
			return this.it.getWindingRule();
		}

		@Override
		public boolean isPolyline() {
			return this.it.isPolyline();
		}
				
	}
	
}