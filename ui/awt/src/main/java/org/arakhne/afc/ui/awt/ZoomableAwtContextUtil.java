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

package org.arakhne.afc.ui.awt;

import java.awt.Font;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import org.arakhne.afc.ui.CenteringTransform;

/** Utilities for ZoomableContext with additional functions
 * dedicated to AWT objects.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public class ZoomableAwtContextUtil extends org.arakhne.afc.ui.ZoomableContextUtil {

	private static void create(
			PathIterator pathIterator,
			CenteringTransform centeringTransform,
			float zoom,
			Path2D output) {
		float[] coords = new float[8];
		while (!pathIterator.isDone()) {
			switch(pathIterator.currentSegment(coords)) {
			case PathIterator.SEG_MOVETO:
				output.moveTo(
						logical2pixel_x(coords[0], centeringTransform, zoom),
						logical2pixel_y(coords[1], centeringTransform, zoom));
				break;
			case PathIterator.SEG_LINETO:
				output.lineTo(
						logical2pixel_x(coords[0], centeringTransform, zoom),
						logical2pixel_y(coords[1], centeringTransform, zoom));
				break;
			case PathIterator.SEG_CUBICTO:
				output.curveTo(
						logical2pixel_x(coords[0], centeringTransform, zoom),
						logical2pixel_y(coords[1], centeringTransform, zoom),
						logical2pixel_x(coords[2], centeringTransform, zoom),
						logical2pixel_y(coords[3], centeringTransform, zoom),
						logical2pixel_x(coords[4], centeringTransform, zoom),
						logical2pixel_y(coords[5], centeringTransform, zoom));
				break;
			case PathIterator.SEG_QUADTO:
				output.quadTo(
						logical2pixel_x(coords[0], centeringTransform, zoom),
						logical2pixel_y(coords[1], centeringTransform, zoom),
						logical2pixel_x(coords[2], centeringTransform, zoom),
						logical2pixel_y(coords[3], centeringTransform, zoom));
				break;
			case PathIterator.SEG_CLOSE:
				output.closePath();
				break;
			default:
				throw new IllegalArgumentException();
			}
			pathIterator.next();
		}
	}

	private static void create2(
			Shape s,
			CenteringTransform centeringTransform,
			float zoom,
			Path2D output) {
		PathIterator it = s.getPathIterator(new AffineTransform());
		float[] coords = new float[8];
		while (!it.isDone()) {
			switch(it.currentSegment(coords)) {
			case PathIterator.SEG_MOVETO:
				output.moveTo(
						pixel2logical_x(coords[0], centeringTransform, zoom),
						pixel2logical_y(coords[1], centeringTransform, zoom));
				break;
			case PathIterator.SEG_LINETO:
				output.lineTo(
						pixel2logical_x(coords[0], centeringTransform, zoom),
						pixel2logical_y(coords[1], centeringTransform, zoom));
				break;
			case PathIterator.SEG_CUBICTO:
				output.curveTo(
						pixel2logical_x(coords[0], centeringTransform, zoom),
						pixel2logical_y(coords[1], centeringTransform, zoom),
						pixel2logical_x(coords[2], centeringTransform, zoom),
						pixel2logical_y(coords[3], centeringTransform, zoom),
						pixel2logical_x(coords[4], centeringTransform, zoom),
						pixel2logical_y(coords[5], centeringTransform, zoom));
				break;
			case PathIterator.SEG_QUADTO:
				output.quadTo(
						pixel2logical_x(coords[0], centeringTransform, zoom),
						pixel2logical_y(coords[1], centeringTransform, zoom),
						pixel2logical_x(coords[2], centeringTransform, zoom),
						pixel2logical_y(coords[3], centeringTransform, zoom));
				break;
			case PathIterator.SEG_CLOSE:
				output.closePath();
				break;
			default:
				throw new IllegalArgumentException();
			}
			it.next();
		}
	}

	/** Replies the transformation matrix which is corresponding to
	 * the given {@link CenteringTransform} and with a zooming
	 * factor.
	 * <p>
	 * <code>
	 * | transform.scaleX*zoom   0                       transform.translationX*zoom |
	 * | 0                       transform.scaleY*zoom   transform.translationY*zoom |
	 * | 0                       0                       1                           |
	 * </code>
	 * 
	 * @param transform
	 * @param zoom
	 * @return the transformation matrix.
	 */
	public static AffineTransform getMatrix(CenteringTransform transform, float zoom) {
		return new AffineTransform(
				transform.getScaleX()*zoom, 0,
				0, transform.getScaleY()*zoom,
				transform.getTranslationX()*zoom,
				transform.getTranslationY()*zoom);
	}
	
	/** Replies the transformation matrix which is corresponding to
	 * the given {@link CenteringTransform} but without a zoom.
	 * <p>
	 * <code>
	 * | transform.scaleX   0                 transform.translationX*zoom |
	 * | 0                  transform.scaleY  transform.translationY*zoom |
	 * | 0                  0                 1                           |
	 * </code>
	 * 
	 * @param transform
	 * @param zoom
	 * @return the transformation matrix.
	 */
	public static AffineTransform getMatrixNoScaling(CenteringTransform transform, float zoom) {
		return new AffineTransform(
				transform.getScaleX(), 0,
				0, transform.getScaleY(),
				transform.getTranslationX()*zoom,
				transform.getTranslationY()*zoom);
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
	public static void logical2pixel(Rectangle2D r,
			CenteringTransform centeringTransform,
			float zoom) {
		assert(r!=null);
		assert(centeringTransform!=null);
		float x = (float)(centeringTransform.isXAxisFlipped() ? r.getMaxX() : r.getMinX());
		float y = (float)(centeringTransform.isYAxisFlipped() ? r.getMaxY() : r.getMinY());
		r.setFrame(
				logical2pixel_x(x, centeringTransform, zoom),
				logical2pixel_y(y, centeringTransform, zoom),
				logical2pixel_size((float)r.getWidth(), zoom),
				logical2pixel_size((float)r.getHeight(), zoom));
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
	public static void pixel2logical(Rectangle2D r,
			CenteringTransform centeringTransform,
			float zoom) {
		assert(r!=null);
		assert(centeringTransform!=null);
		float x = (float)(centeringTransform.isXAxisFlipped() ? r.getMaxX() : r.getMinX());
		float y = (float)(centeringTransform.isYAxisFlipped() ? r.getMaxY() : r.getMinY());
		r.setFrame(
				pixel2logical_x(x, centeringTransform, zoom),
				pixel2logical_y(y, centeringTransform, zoom),
				pixel2logical_size((float)r.getWidth(), zoom),
				pixel2logical_size((float)r.getHeight(), zoom));
	}

	/** Translate the specified font from a logical length to a screen length.
	 * 
	 * @param font is the font with a size in the logical coordinate system.
	 * @param zoom is the current zooming factor of the view.
	 * @return the font with a size suitable for the screen coordinate system.
	 */
	public static Font logical2pixel(Font font, float zoom) {
		float nSize = logical2pixel_size(font.getSize2D(), zoom);
		return font.deriveFont(nSize);
	}

	/** Translate the specified font from a screen length to a logical length.
	 * 
	 * @param font is the font with a size in the screen coordinate system.
	 * @param zoom is the current zooming factor of the view.
	 * @return the font with a size suitable for the logical coordinate system.
	 */
	public static Font pixel2logical(Font font, float zoom) {
		float nSize = pixel2logical_size(font.getSize2D(), zoom);
		return font.deriveFont(nSize);
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
	public static void logical2pixel(Point2D p,
			CenteringTransform centeringTransform,
			float zoom) {
		assert(p!=null);
		assert(centeringTransform!=null);
		p.setLocation(
				logical2pixel_x((float)p.getX(), centeringTransform, zoom),
				logical2pixel_y((float)p.getY(), centeringTransform, zoom));
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
	public static void pixel2logical(Point2D p,
			CenteringTransform centeringTransform,
			float zoom) {
		assert(p!=null);
		assert(centeringTransform!=null);
		p.setLocation(
				pixel2logical_x((float)p.getX(), centeringTransform, zoom),
				pixel2logical_y((float)p.getY(), centeringTransform, zoom));
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
	public static Path2D logical2pixel(PathIterator p,
			CenteringTransform centeringTransform,
			float zoom) {
		Path2D path = new Path2D.Float();
		float[] coords = new float[6];
		while(!p.isDone()) {
			switch(p.currentSegment(coords)) {
			case PathIterator.SEG_MOVETO:
				path.moveTo(
						logical2pixel_x(coords[0], centeringTransform, zoom),
						logical2pixel_y(coords[1], centeringTransform, zoom));
				break;
			case PathIterator.SEG_LINETO:
				path.lineTo(
						logical2pixel_x(coords[0], centeringTransform, zoom),
						logical2pixel_y(coords[1], centeringTransform, zoom));
				break;
			case PathIterator.SEG_QUADTO:
				path.quadTo(
						logical2pixel_x(coords[0], centeringTransform, zoom),
						logical2pixel_y(coords[1], centeringTransform, zoom),
						logical2pixel_x(coords[2], centeringTransform, zoom),
						logical2pixel_y(coords[3], centeringTransform, zoom));
				break;
			case PathIterator.SEG_CUBICTO:
				path.curveTo(
						logical2pixel_x(coords[0], centeringTransform, zoom),
						logical2pixel_y(coords[1], centeringTransform, zoom),
						logical2pixel_x(coords[2], centeringTransform, zoom),
						logical2pixel_y(coords[3], centeringTransform, zoom),
						logical2pixel_x(coords[4], centeringTransform, zoom),
						logical2pixel_y(coords[5], centeringTransform, zoom));
				break;
			case PathIterator.SEG_CLOSE:
				path.closePath();
				break;
			default:
			}
			p.next();
		}
		return path;
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
	public static Path2D pixel2logical(PathIterator p,
			CenteringTransform centeringTransform,
			float zoom) {
		Path2D path = new Path2D.Float();
		float[] coords = new float[6];
		while(!p.isDone()) {
			switch(p.currentSegment(coords)) {
			case PathIterator.SEG_MOVETO:
				path.moveTo(
						pixel2logical_x(coords[0], centeringTransform, zoom),
						pixel2logical_y(coords[1], centeringTransform, zoom));
				break;
			case PathIterator.SEG_LINETO:
				path.lineTo(
						pixel2logical_x(coords[0], centeringTransform, zoom),
						pixel2logical_y(coords[1], centeringTransform, zoom));
				break;
			case PathIterator.SEG_QUADTO:
				path.quadTo(
						pixel2logical_x(coords[0], centeringTransform, zoom),
						pixel2logical_y(coords[1], centeringTransform, zoom),
						pixel2logical_x(coords[2], centeringTransform, zoom),
						pixel2logical_y(coords[3], centeringTransform, zoom));
				break;
			case PathIterator.SEG_CUBICTO:
				path.curveTo(
						pixel2logical_x(coords[0], centeringTransform, zoom),
						pixel2logical_y(coords[1], centeringTransform, zoom),
						pixel2logical_x(coords[2], centeringTransform, zoom),
						pixel2logical_y(coords[3], centeringTransform, zoom),
						pixel2logical_x(coords[4], centeringTransform, zoom),
						pixel2logical_y(coords[5], centeringTransform, zoom));
				break;
			case PathIterator.SEG_CLOSE:
				path.closePath();
				break;
			default:
			}
			p.next();
		}
		return path;
	}

	/** Translates the specified shape
	 *  into the screen space.
	 *
	 * @param <T> is the type of shape to convert.
	 * @param s is the shape in the logical space.
	 * @param centeringTransform is the transform to apply to the points to change from/to the coordinate
	 * system from the "global" logical coordinate system to/from the "centered" logical coordinate
	 * system.
	 * @param zoom is the current zooming factor of the view.
	 * @return a shape into the screen space.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Shape> T logical2pixel(
			T s, CenteringTransform centeringTransform, float zoom) {
		if (s==null) return null;
		SupportedShape type = SupportedShape.getTypeOf(s.getClass());
		try {
			switch(type) {
			case RECTANGLE2D:
			{
				Rectangle2D r = (Rectangle2D)s;
				float x = (float)((centeringTransform.isXAxisFlipped()) ? r.getMaxX() : r.getMinX());
				float y = (float)((centeringTransform.isYAxisFlipped()) ? r.getMaxY() : r.getMinY());
				return (T)new Rectangle2D.Double(
						logical2pixel_x(x, centeringTransform, zoom),
						logical2pixel_y(y, centeringTransform, zoom),
						logical2pixel_size((float)r.getWidth(), zoom),
						logical2pixel_size((float)r.getHeight(), zoom));
			}
			case ROUND_RECTANGLE:
			{
				RoundRectangle2D r = (RoundRectangle2D)s;
				float x = (float)((centeringTransform.isXAxisFlipped()) ? r.getMaxX() : r.getMinX());
				float y = (float)((centeringTransform.isYAxisFlipped()) ? r.getMaxY() : r.getMinY());
				return (T)new RoundRectangle2D.Double(
						logical2pixel_x(x, centeringTransform, zoom),
						logical2pixel_y(y, centeringTransform, zoom),
						logical2pixel_size((float)r.getWidth(), zoom),
						logical2pixel_size((float)r.getHeight(), zoom),
						logical2pixel_size((float)r.getArcWidth(), zoom),
						logical2pixel_size((float)r.getArcHeight(), zoom));
			}
			case ELLIPSE:
			{
				Ellipse2D e = (Ellipse2D)s;
				float x = (float)((centeringTransform.isXAxisFlipped()) ? e.getMaxX() : e.getMinX());
				float y = (float)((centeringTransform.isYAxisFlipped()) ? e.getMaxY() : e.getMinY());
				return (T)new Ellipse2D.Double(
						logical2pixel_x(x, centeringTransform, zoom),
						logical2pixel_y(y, centeringTransform, zoom),
						logical2pixel_size((float)e.getWidth(), zoom),
						logical2pixel_size((float)e.getHeight(), zoom));
			}
			case LINE:
			{
				Line2D l = (Line2D)s;
				return (T)new Line2D.Double(
						logical2pixel_x((float)l.getX1(), centeringTransform, zoom),
						logical2pixel_y((float)l.getY1(), centeringTransform, zoom),
						logical2pixel_x((float)l.getX2(), centeringTransform, zoom),
						logical2pixel_y((float)l.getY2(), centeringTransform, zoom));
			}
			case ARC:
			{
				Arc2D a = (Arc2D)s;
				float x = (float)((centeringTransform.isXAxisFlipped()) ? a.getMaxX() : a.getMinX());
				float y = (float)((centeringTransform.isYAxisFlipped()) ? a.getMaxY() : a.getMinY());
				return (T)new Arc2D.Double(
						logical2pixel_x(x, centeringTransform, zoom),
						logical2pixel_y(y, centeringTransform, zoom),
						logical2pixel_size((float)a.getWidth(), zoom),
						logical2pixel_size((float)a.getHeight(), zoom),
						a.getAngleStart(),
						a.getAngleExtent(),
						a.getArcType());
			}
			case POLYGON:
			{
				Polygon p = (Polygon)s ;
				float x, y;
				GeneralPath p2 = new GeneralPath() ;
				for( int i=0; i < p.npoints; ++i ) {
					x = logical2pixel_x(p.xpoints[i], centeringTransform, zoom);
					y = logical2pixel_y(p.ypoints[i], centeringTransform, zoom);
					if (i==0)
						p2.moveTo(x, y);
					else
						p2.lineTo(x, y);
				}
				p2.closePath();
				return (T)p2 ;
			}
			case CUBIC_CURVE:
			{
				CubicCurve2D c = (CubicCurve2D)s ;
				return (T)new CubicCurve2D.Double(
						logical2pixel_x((float)c.getX1(), centeringTransform, zoom),
						logical2pixel_y((float)c.getY1(), centeringTransform, zoom),
						logical2pixel_x((float)c.getCtrlX1(), centeringTransform, zoom),
						logical2pixel_y((float)c.getCtrlY1(), centeringTransform, zoom),
						logical2pixel_x((float)c.getCtrlX2(), centeringTransform, zoom),
						logical2pixel_y((float)c.getCtrlY2(), centeringTransform, zoom),
						logical2pixel_x((float)c.getX2(), centeringTransform, zoom),
						logical2pixel_y((float)c.getY2(), centeringTransform, zoom));
			}
			case QUAD_CURVE:
			{
				QuadCurve2D c = (QuadCurve2D)s ;
				return (T)new QuadCurve2D.Double(
						logical2pixel_x((float)c.getX1(), centeringTransform, zoom),
						logical2pixel_y((float)c.getY1(), centeringTransform, zoom),
						logical2pixel_x((float)c.getCtrlX(), centeringTransform, zoom),
						logical2pixel_y((float)c.getCtrlY(), centeringTransform, zoom),
						logical2pixel_x((float)c.getX2(), centeringTransform, zoom),
						logical2pixel_y((float)c.getY2(), centeringTransform, zoom));
			}
			case PATH:
			{
				Path2D p = (Path2D)s;
				Path2D p2 = new GeneralPath(p.getWindingRule());
				create(
						p.getPathIterator(null), centeringTransform, zoom, p2);
				return (T)p2;
			}
			case AREA:
			{
				Area a = (Area)s ;
				GeneralPath p2 = new GeneralPath();
				create(
						a.getPathIterator(null),
						centeringTransform, zoom, p2);
				return (T)new Area(p2);
			}
			case VIRTUALIZABLE_SHAPE:
			{
				VirtualizableShape vs = (VirtualizableShape)s;
				return (T)vs.toScreen(centeringTransform, zoom);
			}
			default:
				throw new IllegalArgumentException();
			}
		}
		catch(ClassCastException exception) {
			//
		}

		throw new UnsupportedShapeException(type);
	}

	/** Translates the specified shape
	 *  into the logical space.
	 *
	 * @param <T> is the type of shape to convert.
	 * @param s is the shape in the screen space.
	 * @param centeringTransform is the transform to apply to the points to change from/to the coordinate
	 * system from the "global" logical coordinate system to/from the "centered" logical coordinate
	 * system.
	 * @param zoom is the current zooming factor of the view.
	 * @return a shape into the logical space.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Shape> T pixel2logical(
			T s, CenteringTransform centeringTransform, float zoom) {
		if (s==null) return null;
		SupportedShape type = SupportedShape.getTypeOf(s.getClass());
		try {
			switch(type) {
			case RECTANGLE2D:
			{
				Rectangle2D r = (Rectangle2D)s;
				float x = (float)((centeringTransform.isXAxisFlipped()) ? r.getMaxX() : r.getMinX());
				float y = (float)((centeringTransform.isYAxisFlipped()) ? r.getMaxY() : r.getMinY());
				return (T)new Rectangle2D.Double(
						pixel2logical_x(x, centeringTransform, zoom),
						pixel2logical_y(y, centeringTransform, zoom),
						pixel2logical_size((float)r.getWidth(), zoom),
						pixel2logical_size((float)r.getHeight(), zoom));
			}
			case ROUND_RECTANGLE:
			{
				RoundRectangle2D r = (RoundRectangle2D)s;
				float x = (float) ((centeringTransform.isXAxisFlipped()) ? r.getMaxX() : r.getMinX());
				float y = (float) ((centeringTransform.isYAxisFlipped()) ? r.getMaxY() : r.getMinY());
				return (T)new RoundRectangle2D.Double(
						pixel2logical_x(x, centeringTransform, zoom),
						pixel2logical_y(y, centeringTransform, zoom),
						pixel2logical_size((float)r.getWidth(), zoom),
						pixel2logical_size((float)r.getHeight(), zoom),
						pixel2logical_size((float)r.getArcWidth(), zoom),
						pixel2logical_size((float)r.getArcHeight(), zoom));
			}
			case ELLIPSE:
			{
				Ellipse2D e = (Ellipse2D)s;
				float x = (float) ((centeringTransform.isXAxisFlipped()) ? e.getMaxX() : e.getMinX());
				float y = (float) ((centeringTransform.isYAxisFlipped()) ? e.getMaxY() : e.getMinY());
				return (T)new Ellipse2D.Double(
						pixel2logical_x(x, centeringTransform, zoom),
						pixel2logical_y(y, centeringTransform, zoom),
						pixel2logical_size((float)e.getWidth(), zoom),
						pixel2logical_size((float)e.getHeight(), zoom));
			}
			case LINE:
			{
				Line2D l = (Line2D)s;
				return (T)new Line2D.Double(
						pixel2logical_x((float)l.getX1(), centeringTransform, zoom),
						pixel2logical_y((float)l.getY1(), centeringTransform, zoom),
						pixel2logical_x((float)l.getX2(), centeringTransform, zoom),
						pixel2logical_y((float)l.getY2(), centeringTransform, zoom));
			}
			case ARC:
			{
				Arc2D a = (Arc2D)s;
				float x = (float) ((centeringTransform.isXAxisFlipped()) ? a.getMaxX() : a.getMinX());
				float y = (float) ((centeringTransform.isYAxisFlipped()) ? a.getMaxY() : a.getMinY());
				return (T)new Arc2D.Double(
						pixel2logical_x(x, centeringTransform, zoom),
						pixel2logical_y(y, centeringTransform, zoom),
						pixel2logical_size((float)a.getWidth(), zoom),
						pixel2logical_size((float)a.getHeight(), zoom),
						a.getAngleStart(),
						a.getAngleExtent(),
						a.getArcType());
			}
			case POLYGON:
			{
				Polygon p = (Polygon)s ;
				GeneralPath p2 = new GeneralPath() ;
				float x, y;
				for( int i=0; i < p.npoints; ++i ) {
					x = pixel2logical_x(p.xpoints[i], centeringTransform, zoom);
					y = pixel2logical_y(p.ypoints[i], centeringTransform, zoom);
					if (i==0) p2.moveTo(x, y);
					else p2.lineTo(x, y);
				}
				p2.closePath();
				return (T)p2 ;
			}
			case CUBIC_CURVE:
			{
				CubicCurve2D c = (CubicCurve2D)s ;
				return (T)new CubicCurve2D.Double(
						pixel2logical_x((float)c.getX1(), centeringTransform, zoom),
						pixel2logical_y((float)c.getY1(), centeringTransform, zoom),
						pixel2logical_x((float)c.getCtrlX1(), centeringTransform, zoom),
						pixel2logical_y((float)c.getCtrlY1(), centeringTransform, zoom),
						pixel2logical_x((float)c.getCtrlX2(), centeringTransform, zoom),
						pixel2logical_y((float)c.getCtrlY2(), centeringTransform, zoom),
						pixel2logical_x((float)c.getX2(), centeringTransform, zoom),
						pixel2logical_y((float)c.getY2(), centeringTransform, zoom));
			}
			case QUAD_CURVE:
			{
				QuadCurve2D c = (QuadCurve2D)s ;
				return (T)new QuadCurve2D.Double(
						pixel2logical_x((float)c.getX1(), centeringTransform, zoom),
						pixel2logical_y((float)c.getY1(), centeringTransform, zoom),
						pixel2logical_x((float)c.getCtrlX(), centeringTransform, zoom),
						pixel2logical_y((float)c.getCtrlY(), centeringTransform, zoom),
						pixel2logical_x((float)c.getX2(), centeringTransform, zoom),
						pixel2logical_y((float)c.getY2(), centeringTransform, zoom));
			}
			case PATH:
			{
				Path2D p = (Path2D)s;
				Path2D p2 = new GeneralPath(p.getWindingRule());
				create2(p, centeringTransform, zoom, p2);
				return (T)p2;
			}
			case AREA:
			{
				Area a = (Area)s ;
				GeneralPath p2 = new GeneralPath();
				create2(a, centeringTransform, zoom, p2);
				return (T)new Area(p2);
			}
			case VIRTUALIZABLE_SHAPE:
			{
				VirtualizableShape vs = (VirtualizableShape)s;
				return (T)vs.fromScreen(centeringTransform, zoom);
			}
			default:
				throw new IllegalArgumentException();
			}
		}
		catch(ClassCastException exception) {
			//
		}

		throw new UnsupportedShapeException(type);
	}

}