/* $Id$
 * 
 * This is a part of NetEditor project from Arakhne.org:
 * package org.arakhne.neteditor.zoompanel.
 * 
 * Copyright (C) 2002  St&eacute;phane GALLAND, Mahdi Hannoun
 * Copyright (C) 2004-2013  St&eacute;phane GALLAND
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
package org.arakhne.afc.ui.swing.zoompanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.GlyphVector;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.plaf.FontUIResource;

import org.arakhne.afc.math.continous.object2d.Circle2f;
import org.arakhne.afc.math.continous.object2d.Ellipse2f;
import org.arakhne.afc.math.continous.object2d.PathIterator2f;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.math.continous.object2d.RoundRectangle2f;
import org.arakhne.afc.math.continous.object2d.Segment2f;
import org.arakhne.afc.math.continous.object2d.Shape2f;
import org.arakhne.afc.ui.CenteringTransform;
import org.arakhne.afc.ui.Graphics2DLOD;
import org.arakhne.afc.ui.StringAnchor;
import org.arakhne.afc.ui.TextAlignment;
import org.arakhne.afc.ui.ZoomableContext;
import org.arakhne.afc.ui.ZoomableContextUtil;
import org.arakhne.afc.ui.awt.AbstractLODGraphics2D;
import org.arakhne.afc.ui.awt.AwtUtil;
import org.arakhne.afc.ui.awt.DoubleDimension;
import org.arakhne.afc.ui.awt.VirtualScreenGraphics2D;
import org.arakhne.afc.ui.awt.ZoomableAwtContextUtil;

/** This graphic context permits to display
 *  something with a good zoom look.
 *  <p>
 *  This class was strongly inspirated by
 *  the zoomable graphics context of 
 *  <a href="http://www.arakhne.org/rubriques.php3?id_rubrique=15">Arakhn&ecirc; NetEditor</a>.
 *  with the authorization of the original authors.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ZoomableGraphics2D extends AbstractLODGraphics2D<Graphics2D> implements VirtualScreenGraphics2D {

	////////////////////////////////////////////////////////////
	// Constants

	/** Indicates if by default the {@link ZoomableGraphics2D} enables image scaling.
	 */
	public static boolean DEFAULT_IMAGE_SCALING = true;

	/** Indicates if by default the {@link ZoomableGraphics2D} enables icon scaling.
	 */
	public static boolean DEFAULT_ICON_SCALING = false;

	/** Indicates if by default the {@link ZoomableGraphics2D} enables icon centering.
	 */
	public static boolean DEFAULT_ICON_CENTERING = true;

	/** Default scaling factor applied to the application's font
	 * to obtain the default size for the fonts inside the zoomable panel.
	 */
	public static float DEFAULT_FONT_SCALE_FACTOR = .8f;

	////////////////////////////////////////////////////////////
	// Attributes

	/** Transformation to center the objects in the view.
	 */
	protected final CenteringTransform centeringTransform;

	/** This is the zoom factor.
	 */
	protected final float zoomFactor;

	/** Scaling sensitivity.
	 */
	private final float scalingSensitivity;

	/** Min scaling factor.
	 */
	private final float minScalingFactor;

	/** Max scaling factor.
	 */
	private final float maxScalingFactor;

	/** Focus point.
	 */
	private final float focusX;

	/** Focus point.
	 */
	private final float focusY;

	/** Indicates if drawn images must be scaled or not.
	 */
	private boolean scaleImage = DEFAULT_IMAGE_SCALING;

	/** Indicates if drawn icons must be scaled or not.
	 */
	private boolean scaleIcon = DEFAULT_ICON_SCALING;

	/** Indicates if drawn icons must be center on the painting coordinates or not.
	 */
	private boolean centerIcon = DEFAULT_ICON_CENTERING;

	private final float defaultFontSize;
	private final Rectangle2D tmpRectangle = new Rectangle2D.Float();

	////////////////////////////////////////////////////////////
	// Constructor

	/** Construct a new ZoomableGraphics2D.
	 *
	 * @param target is the graphics in which draws will be done.
	 * @param target_component is the target component (used to display an icon).
	 * @param centeringTransform is the transformation used to center the objects in the view.
	 * @param zoom is the zoom factor.
	 * @param antialiasing permits to force the anti-aliasing flag for the target graphical context
	 * @param is_for_printing indicates if this graphics environment is for printing or not.
	 * @param lod indicates the desired LOD used by this graphical context.
	 * @param scalingSensitivity is the sensitivity of the zooming functions.
	 * @param minScalingFactor is the minimal scaling factor.
	 * @param maxScalingFactor is the maximal scaling factor.
	 * @param focusX is the X coordinate of the point that is at the center of the view.
	 * @param focusY is the Y coordinate of the point that is at the center of the view.
	 */
	public ZoomableGraphics2D(
			Graphics2D target,
			Component target_component,
			CenteringTransform centeringTransform,
			float zoom,
			boolean antialiasing,
			boolean is_for_printing, 
			Graphics2DLOD lod,
			float scalingSensitivity,
			float minScalingFactor,
			float maxScalingFactor,
			float focusX, float focusY) {
		super(target, target_component, antialiasing, is_for_printing, lod);
		this.centeringTransform = centeringTransform;
		this.scalingSensitivity = scalingSensitivity;
		this.minScalingFactor = minScalingFactor;
		this.maxScalingFactor = maxScalingFactor;
		this.focusX = focusX;
		this.focusY = focusY;
		this.defaultFontSize = DEFAULT_FONT_SCALE_FACTOR * target.getFont().getSize2D();
		this.zoomFactor = zoom;
		this.target.setFont(ZoomableAwtContextUtil.logical2pixel(
				getOriginalUnscaledFont(),
				this.zoomFactor));
	}

	/** Construct a new ZoomableGraphics2D.
	 *
	 * @param target is the graphics in which draws will be done.
	 * @param target_component is the target component (used to display an icon).
	 * @param centeringTransform is the transformation used to center the objects in the view.
	 * @param zoom is the zoom factor.
	 * @param antialiasing permits to force the anti-aliasing flag for the target graphical context
	 * @param lod indicates the desired LOD used by this graphical context.
	 * @param scalingSensitivity is the sensitivity of the zooming functions.
	 * @param minScalingFactor is the minimal scaling factor.
	 * @param maxScalingFactor is the maximal scaling factor.
	 * @param focusX is the X coordinate of the point that is at the center of the view.
	 * @param focusY is the Y coordinate of the point that is at the center of the view.
	 */
	public ZoomableGraphics2D(
			Graphics2D target,
			Component target_component,
			CenteringTransform centeringTransform,
			float zoom, 
			boolean antialiasing,
			Graphics2DLOD lod,
			float scalingSensitivity,
			float minScalingFactor,
			float maxScalingFactor,
			float focusX,
			float focusY) {
		super(target, target_component, antialiasing, lod);
		this.centeringTransform = centeringTransform;
		this.scalingSensitivity = scalingSensitivity;
		this.minScalingFactor = minScalingFactor;
		this.maxScalingFactor = maxScalingFactor;
		this.focusX = focusX;
		this.focusY = focusY;
		this.defaultFontSize = getFont().getSize2D();
		this.zoomFactor = zoom;
		this.target.setFont(ZoomableAwtContextUtil.logical2pixel(
				getOriginalUnscaledFont(),
				this.zoomFactor));
	}

	/** Construct a new ZoomableGraphics2D.
	 *
	 * @param target is the graphics in which draws will be done.
	 * @param target_component is the target component (used to display an icon).
	 * @param centeringTransform is the transformation used to center the objects in the view.
	 * @param zoom is the zoom factor.
	 * @param lod indicates the desired LOD used by this graphical context.
	 * @param scalingSensitivity is the sensitivity of the zooming functions.
	 * @param minScalingFactor is the minimal scaling factor.
	 * @param maxScalingFactor is the maximal scaling factor.
	 * @param focusX is the X coordinate of the point that is at the center of the view.
	 * @param focusY is the Y coordinate of the point that is at the center of the view.
	 */
	public ZoomableGraphics2D(
			Graphics2D target,
			Component target_component,
			CenteringTransform centeringTransform,
			float zoom, 
			Graphics2DLOD lod,
			float scalingSensitivity,
			float minScalingFactor,
			float maxScalingFactor,
			float focusX,
			float focusY) {
		super(target, target_component, lod);
		this.centeringTransform = centeringTransform;
		this.scalingSensitivity = scalingSensitivity;
		this.minScalingFactor = minScalingFactor;
		this.maxScalingFactor = maxScalingFactor;
		this.focusX = focusX;
		this.focusY = focusY;
		this.defaultFontSize = getFont().getSize2D();
		this.zoomFactor = zoom;
		this.target.setFont(ZoomableAwtContextUtil.logical2pixel(
				getOriginalUnscaledFont(),
				this.zoomFactor));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected float polyX(float x) {
		return logical2pixel_x(x);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected float polyY(float y) {
		return logical2pixel_y(y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final StringAnchor getStringAnchor() {
		return StringAnchor.UPPER_LEFT;
	}

	/** Replies if this figure could drawn or not.
	 * <p>
	 * This function assumes that a drawable figure
	 * must have a bounding box with its width and
	 * height greaters or equals to a specified size.
	 * This size depends on the adviced level of details:
	 * {@link Graphics2DLOD#LOW_LEVEL_OF_DETAIL},
	 * {@link Graphics2DLOD#NORMAL_LEVEL_OF_DETAIL}
	 * or {@link Graphics2DLOD#HIGH_LEVEL_OF_DETAIL}.
	 *
	 * @param rect is the rect to draw.
	 * @return <code>true</code> if the figure is drawable, otherwhise
	 * <code>false</code>.
	 */ 
	@Override
	public boolean hit(Rectangle2D rect) {
		return super.hit(logical2pixel(rect));
	}

	////////////////////////////////////////////////////////////
	// Graphics API

	/** Creates the current graphics context.
	 */
	@Override
	public Graphics create() {
		ZoomableGraphics2D z = new ZoomableGraphics2D((Graphics2D)this.target.create(),
				this.targetComponent.get(),
				this.centeringTransform,
				this.zoomFactor,
				isAntiAliased(),
				isPrinting(),
				getLOD(),
				this.scalingSensitivity,
				this.minScalingFactor,
				this.maxScalingFactor,
				this.focusX,
				this.focusY) ;
		z.setImageOriginalSize(isImageOriginalSize());
		z.setIconCentered(isIconCentered());
		z.setIconOriginalSize(isIconOriginalSize());
		return z;
	}

	/** Clears the specified rectangle by filling with the 
	 *  background color.
	 *  <p>
	 *  The given coordinates will be transformed.
	 *
	 * @param x x coordinate of the rectangle
	 * @param y y coordinate of the rectangle
	 * @param width the width of the rectangle
	 * @param height the height of the rectangle
	 */
	@Override
	public void clearRect( float x, float y, float width, float height ) {
		this.tmpRectangle.setRect(x, y, width, height);
		ZoomableAwtContextUtil.logical2pixel(this.tmpRectangle,
				this.centeringTransform, this.zoomFactor);
		this.target.clearRect(
				(int)this.tmpRectangle.getMinX(),
				(int)this.tmpRectangle.getMinY(),
				(int)this.tmpRectangle.getWidth(),
				(int)this.tmpRectangle.getHeight());
	}

	/** Intersects the current clip with the specified rectangle.
	 *  <p>
	 *  The given coordinates will <em>not</em> be transformed.
	 *
	 * @param x x coordinate of the rectangle
	 * @param y y coordinate of the rectangle
	 * @param width the width of the rectangle
	 * @param height the height of the rectangle	
	 */
	@Override
	public void clipRect( float x, float y, float width, float height ) {
		this.tmpRectangle.setRect(x, y, width, height);
		ZoomableAwtContextUtil.logical2pixel(this.tmpRectangle,
				this.centeringTransform, this.zoomFactor);
		Rectangle2D c = new Rectangle2D.Double(
				this.tmpRectangle.getMinX(),
				this.tmpRectangle.getMinY(),
				this.tmpRectangle.getWidth(),
				this.tmpRectangle.getHeight());
		this.target.clip(c);
	}

	/** Copies an area of the component by a distance of 
	 *  specified <var>dx</var> and <var>dy</var>.
	 *  <p>
	 *  This method is not supported in a vectorial context.
	 *
	 * @param x x coordinate of the area
	 * @param y y coordinate of the area
	 * @param width the width of the area
	 * @param height the height of the area
	 * @param dx horizontal translation
	 * @param dy vertical translation
	 */
	@Override
	public void copyArea( float x, float y, float width, float height, float dx, float dy ) {
		this.tmpRectangle.setRect(x, y, width, height);
		ZoomableAwtContextUtil.logical2pixel(this.tmpRectangle,
				this.centeringTransform, this.zoomFactor);
		this.target.copyArea(
				(int)this.tmpRectangle.getMinX(),
				(int)this.tmpRectangle.getMinY(),
				(int)this.tmpRectangle.getWidth(),
				(int)this.tmpRectangle.getHeight(),
				(int)ZoomableContextUtil.logical2pixel_size(dx, this.zoomFactor),
				(int)ZoomableContextUtil.logical2pixel_size(dy, this.zoomFactor)) ;
	}

	/** Draws the outline of an arc.
	 *  <p>
	 *  The given coordinates will be transformed.
	 *
	 * @param x x coordinate of the covered rectangle
	 * @param y y coordinate of the covered rectangle
	 * @param width the width of the covered rectangle
	 * @param height the height of the covered rectangle
	 * @param startAngle the starting angle
	 * @param arcAngle the size of the arc
	 */
	@Override
	public void drawArc( float x, float y, float width, float height, float startAngle, float arcAngle ) {
		this.tmpRectangle.setRect(x, y, width, height);
		ZoomableAwtContextUtil.logical2pixel(this.tmpRectangle,
				this.centeringTransform, this.zoomFactor);
		Arc2D arc = new Arc2D.Double(
				this.tmpRectangle.getMinX(),
				this.tmpRectangle.getMinY(),
				this.tmpRectangle.getWidth(),
				this.tmpRectangle.getHeight(),
				startAngle,
				arcAngle,
				Arc2D.OPEN);
		this.target.draw(arc);
	}

	/** Draws the specified image.
	 *  <p>
	 *  The given coordinates will be transformed.
	 * <p>
	 * Caution: this function does not support
	 * the scaling of the image. So the given image
	 * will be drawn with its size as in the screen-coordinate space.
	 *
	 * @param img the image
	 * @param x horizontal location of the image
	 * @param y vertical location of the image
	 * @param bgColor the color of transparent pixels
	 * @param obs the notified observer
	 * @return <code>true</code> if theimage wascomplety drawn.
	 * @see #setImageOriginalSize(boolean)
	 * @see #isImageOriginalSize()
	 */
	@Override
	public boolean drawImage( Image img, float x, float y, Color bgColor, ImageObserver obs ) {
		int imgWidth = img.getWidth(obs);
		if (imgWidth==-1) return false;
		int imgHeight = img.getHeight(obs);
		if (imgHeight==-1) return false;

		Image drawableImage;

		if (this.scaleImage) {
			this.tmpRectangle.setRect(x, y, imgWidth, imgHeight);
		}
		else {
			float sx = ZoomableContextUtil.pixel2logical_size(imgWidth, this.zoomFactor);
			float sy = ZoomableContextUtil.pixel2logical_size(imgHeight, this.zoomFactor);
			this.tmpRectangle.setRect(x, y, sx, sy);
		}

		ZoomableAwtContextUtil.logical2pixel(this.tmpRectangle,
				this.centeringTransform, this.zoomFactor);

		if (this.tmpRectangle.isEmpty()) return true;

		if (this.scaleImage) {
			int sx = (int)this.tmpRectangle.getWidth();
			int sy = (int)this.tmpRectangle.getHeight();
			drawableImage = new BufferedImage(sx, sy, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D)drawableImage.getGraphics();
			if (!g.drawImage(img, 0, 0, sx, sy, 0, 0, imgWidth, imgHeight, obs)) {
				g.dispose();
				return false;
			}
			g.dispose();
		}
		else {
			drawableImage = img;
		}

		int ix = (int)this.tmpRectangle.getMinX();
		int iy = (int)this.tmpRectangle.getMinX();

		return this.target.drawImage(drawableImage, ix, iy, bgColor, obs);
	}

	/** Draws the specified image with its upper-left corner
	 * at the given position.
	 *  <p>
	 *  The width and height of the given image
	 *  is expressed according to the value
	 *  replied by {@link #isImageOriginalSize()}.
	 *  If {@link #isImageOriginalSize()} replies <code>true</code>
	 *  the image width and height are assumed to be pixels. 
	 *  If {@link #isImageOriginalSize()} replies <code>false</code>
	 *  the image width and height are assumed to be meters. 
	 *
	 * @param img the image
	 * @param x horizontal location of the image
	 * @param y vertical location of the image
	 * @param obs the notified observer
	 * @return <code>true</code> if the image was complety drawn.
	 * @see #setImageOriginalSize(boolean)
	 * @see #isImageOriginalSize()
	 * @see #isImageOriginalSize()
	 */
	@Override
	public boolean drawImage( Image img, float x, float y, ImageObserver obs ) {
		int imgWidth = img.getWidth(obs);
		if (imgWidth==-1) return false;
		int imgHeight = img.getHeight(obs);
		if (imgHeight==-1) return false;

		Image drawableImage;

		if (this.scaleImage) {
			this.tmpRectangle.setRect(x, y, imgWidth, imgHeight);
		}
		else {
			float sx = ZoomableContextUtil.pixel2logical_size(imgWidth, this.zoomFactor);
			float sy = ZoomableContextUtil.pixel2logical_size(imgHeight, this.zoomFactor);
			this.tmpRectangle.setRect(x, y, sx, sy);
		}

		ZoomableAwtContextUtil.logical2pixel(this.tmpRectangle,
				this.centeringTransform, this.zoomFactor);

		if (this.tmpRectangle.isEmpty()) return true;

		if (this.scaleImage) {
			int sx = (int)this.tmpRectangle.getWidth();
			int sy = (int)this.tmpRectangle.getHeight();
			drawableImage = new BufferedImage(sx, sy, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D)drawableImage.getGraphics();
			if (!g.drawImage(img, 0, 0, sx, sy, 0, 0, imgWidth, imgHeight, obs)) {
				g.dispose();
				return false;
			}
			g.dispose();
		}
		else {
			drawableImage = img;
		}

		int ix = (int)this.tmpRectangle.getMinX();
		int iy = (int)this.tmpRectangle.getMinY();

		return this.target.drawImage(drawableImage, ix, iy, obs);
	}

	/** Draws the specified image. Scales it if necessary.
	 *
	 * @param img the image
	 * @param x horizontal location of the image
	 * @param y vertical location of the image
	 * @param width width of the image
	 * @param height height of the image
	 * @param bgColor the color of transparent pixels
	 * @param obs the notified observer
	 * @return <code>true</code> if theimage wascomplety drawn.
	 * @see #setImageOriginalSize(boolean)
	 * @see #isImageOriginalSize()
	 */
	@Override
	public boolean drawImage( Image img, float x, float y, float width, float height, Color bgColor, ImageObserver obs ) {
		this.tmpRectangle.setRect(x, y, width, height);
		ZoomableAwtContextUtil.logical2pixel(this.tmpRectangle,
				this.centeringTransform, this.zoomFactor);

		int w, h;
		if (this.scaleImage) {
			w = (int)this.tmpRectangle.getWidth();
			h = (int)this.tmpRectangle.getHeight();
		}
		else {
			w = (int)width;
			h = (int)height;
		}

		return this.target.drawImage( img, 
				(int)this.tmpRectangle.getMinX(),
				(int)this.tmpRectangle.getMinY(),
				w, h,
				bgColor, obs ) ;
	}

	/** Draws the specified image. Scales it if necessary.
	 *  <p>
	 *  The given coordinates will be transformed.
	 *
	 * @param img the image
	 * @param x horizontal location of the image
	 * @param y vertical location of the image
	 * @param width width of the image
	 * @param height height of the image
	 * @param obs the notified observer
	 * @return <code>true</code> if theimage wascomplety drawn.
	 * @see #setImageOriginalSize(boolean)
	 * @see #isImageOriginalSize()
	 */
	@Override
	public boolean drawImage( Image img, float x, float y, float width, float height, ImageObserver obs ) {
		this.tmpRectangle.setRect(x, y, width, height);
		ZoomableAwtContextUtil.logical2pixel(this.tmpRectangle,
				this.centeringTransform, this.zoomFactor);

		int w, h;
		if (this.scaleImage) {
			w = (int)this.tmpRectangle.getWidth();
			h = (int)this.tmpRectangle.getHeight();
		}
		else {
			w = (int)width;
			h = (int)height;
		}

		return this.target.drawImage( img, 
				(int)this.tmpRectangle.getMinX(),
				(int)this.tmpRectangle.getMinY(),
				w, h,
				obs ) ;
	}

	/** Draws a part of the specified image into
	 *  the given rectangle. Scales it if necessary.
	 *  <p>
	 *  The given target coordinates will be transformed.
	 *
	 * @param img the image
	 * @param dx1 horizontal location of the target image upper left corner
	 * @param dy1 vertical location of the target image upper left corner
	 * @param dx2 horizontal location of the target image lower right corner
	 * @param dy2 vertical location of the target image lower right corner
	 * @param sx1 horizontal location of the source image upper left corner
	 * @param sy1 vertical location of the source image upper left corner
	 * @param sx2 horizontal location of the source image lower right corner
	 * @param sy2 vertical location of the source image lower right corner
	 * @param bgColor the color of transparent pixels
	 * @param obs the notified observer
	 * @return <code>true</code> if theimage wascomplety drawn.
	 * @see #setImageOriginalSize(boolean)
	 * @see #isImageOriginalSize()
	 */
	@Override
	public boolean drawImage( Image img, 
			float dx1, float dy1, 
			float dx2, float dy2,
			int sx1, int sy1,
			int sx2, int sy2,
			Color bgColor,
			ImageObserver obs ) {
		this.tmpRectangle.setRect(Math.min(dx1, dx2), Math.min(dy1, dy2), Math.abs(dx2-dx1), Math.abs(dy2-dy1));
		ZoomableAwtContextUtil.logical2pixel(this.tmpRectangle,
				this.centeringTransform, this.zoomFactor);

		return this.target.drawImage( img, 
				(int)((dx1<=dx2) ? this.tmpRectangle.getMinX() : this.tmpRectangle.getMaxX()),
				(int)((dx1<=dx2) ? this.tmpRectangle.getMinY() : this.tmpRectangle.getMaxY()), 
				(int)((dx1<=dx2) ? this.tmpRectangle.getMaxX() : this.tmpRectangle.getMinX()),
				(int)((dx1<=dx2) ? this.tmpRectangle.getMaxY() : this.tmpRectangle.getMinY()), 
				sx1, sy1,
				sx2, sy2,
				bgColor, obs ) ;
	}

	/** Draws a part of the specified image into
	 *  the given rectangle. Scales it if necessary.
	 *  <p>
	 *  The given coordinates will be transformed.
	 *
	 * @param img the image
	 * @param dx1 horizontal location of the target image upper left corner
	 * @param dy1 vertical location of the target image upper left corner
	 * @param dx2 horizontal location of the target image lower right corner
	 * @param dy2 vertical location of the target image lower right corner
	 * @param sx1 horizontal location of the source image upper left corner
	 * @param sy1 vertical location of the source image upper left corner
	 * @param sx2 horizontal location of the source image lower right corner
	 * @param sy2 vertical location of the source image lower right corner
	 * @param obs the notified observer
	 * @return <code>true</code> if theimage wascomplety drawn.
	 * @see #setImageOriginalSize(boolean)
	 * @see #isImageOriginalSize()
	 */
	@Override
	public boolean drawImage( Image img, 
			float dx1, float dy1, 
			float dx2, float dy2,
			int sx1, int sy1,
			int sx2, int sy2,
			ImageObserver obs ) {
		this.tmpRectangle.setRect(Math.min(dx1, dx2), Math.min(dy1, dy2), Math.abs(dx2-dx1), Math.abs(dy2-dy1));
		ZoomableAwtContextUtil.logical2pixel(this.tmpRectangle,
				this.centeringTransform, this.zoomFactor);

		return this.target.drawImage( img, 
				(int)((dx1<=dx2) ? this.tmpRectangle.getMinX() : this.tmpRectangle.getMaxX()),
				(int)((dx1<=dx2) ? this.tmpRectangle.getMinY() : this.tmpRectangle.getMaxY()), 
				(int)((dx1<=dx2) ? this.tmpRectangle.getMaxX() : this.tmpRectangle.getMinX()),
				(int)((dx1<=dx2) ? this.tmpRectangle.getMaxY() : this.tmpRectangle.getMinY()), 
				sx1, sy1,
				sx2, sy2,
				obs ) ;
	}

	/** Draws an icon.
	 *
	 * @param icon is the icon to paint
	 * @param x is the location where to paint the icon.
	 * @param y is the location where to paint the icon.
	 * @see #setIconOriginalSize(boolean)
	 * @see #setIconCentered(boolean)
	 */
	public void drawIcon( Icon icon, float x, float y) {
		Icon drawableIcon;

		if (this.scaleIcon) {
			this.tmpRectangle.setRect(x, y, icon.getIconWidth(), icon.getIconHeight());
		}
		else {
			float sx = ZoomableContextUtil.pixel2logical_size(icon.getIconWidth(), this.zoomFactor);
			float sy = ZoomableContextUtil.pixel2logical_size(icon.getIconHeight(), this.zoomFactor);
			this.tmpRectangle.setRect(x, y, sx, sy);
		}

		ZoomableAwtContextUtil.logical2pixel(this.tmpRectangle,
				this.centeringTransform, this.zoomFactor);

		if (this.tmpRectangle.isEmpty()) return;

		if (this.scaleIcon && icon instanceof ImageIcon) {
			Image img = AwtUtil.computeScaledImage(
					(ImageIcon)icon, 
					(int)this.tmpRectangle.getWidth(),
					(int)this.tmpRectangle.getHeight(),
					this.targetComponent.get());
			drawableIcon = new ImageIcon(img);
		}
		else {
			drawableIcon = icon;
		}

		int ix = (int)this.tmpRectangle.getMinX();
		int iy = (int)this.tmpRectangle.getMinY();
		if (this.centerIcon) {
			ix -= drawableIcon.getIconWidth() / 2;
			iy -= drawableIcon.getIconHeight() / 2;
		}

		drawableIcon.paintIcon(
				this.targetComponent.get(),
				this.target, 
				ix, iy);
	}

	/** Draws an icon.
	 *  <p>
	 *  The given coordinates will be transformed.
	 *  <p>
	 *  Icon is not scaled. Icon size is assumed to
	 *  be pixel-based.
	 *
	 * @param icon is the icon to paint
	 * @param x is the location where to paint the icon.
	 * @param y is the location where to paint the icon.
	 * @see #setIconOriginalSize(boolean)
	 * @see #setIconCentered(boolean)
	 */
	public final void drawIcon( Icon icon, int x, int y ) {
		drawIcon(icon, (float)x, (float)y);
	}

	/** Draws a line, using the current color, between
	 *  (<var>x1</var>,<var>y1</var>) and 
	 *  (<var>x2</var>,<var>y2</var>).
	 *  <p>
	 *  The given coordinates will be transformed.
	 *
	 * @param x1 horizontal location of the start point.
	 * @param y1 vertical location of the start point.
	 * @param x2 horizontal location of the target point.
	 * @param y2 vertical location of the target point.
	 */
	@Override
	public void drawLine( float x1, float y1, float x2, float y2 ) {
		Line2D line = new Line2D.Double(
				logical2pixel_x( x1 ), logical2pixel_y( y1 ),
				logical2pixel_x( x2 ), logical2pixel_y( y2 ) ) ;
		this.target.draw(line);
	}

	/** Draws an oval, using the current color, inside
	 *  the specified rectangle.
	 *  <p>
	 *  The given coordinates will be transformed.
	 *
	 * @param x horizontal location of the enclosing rectangle.
	 * @param y vertical location of the enclosing rectangle.
	 * @param w width of the enclosing rectangle.
	 * @param h height of the enclosing rectangle.
	 */
	@Override
	public void drawOval( float x, float y, float w, float h ) {
		this.tmpRectangle.setRect(x, y, w, h);
		ZoomableAwtContextUtil.logical2pixel(this.tmpRectangle,
				this.centeringTransform, this.zoomFactor);
		Ellipse2D oval = new Ellipse2D.Double(
				this.tmpRectangle.getMinX(),
				this.tmpRectangle.getMinY(),
				this.tmpRectangle.getWidth(),
				this.tmpRectangle.getHeight()) ;
		this.target.draw(oval);
	}

	/** Draws the outline of a rounded-rectangle.
	 *  <p>
	 *  The given coordinates will be transformed.
	 *
	 * @param x horizontal location of the rectangle.
	 * @param y vertical location of the rectangle.
	 * @param w width of the rectangle.
	 * @param h height of the rectangle.
	 * @param arcWidth width of the corners' angle.
	 * @param arcHeight height of the corners' angle.
	 */
	@Override
	public void drawRoundRect( float x, float y, float w, float h, float arcWidth, float arcHeight ) {
		this.tmpRectangle.setRect(x, y, w, h);
		ZoomableAwtContextUtil.logical2pixel(this.tmpRectangle,
				this.centeringTransform, this.zoomFactor);
		RoundRectangle2D r = new RoundRectangle2D.Double(
				this.tmpRectangle.getMinX(),
				this.tmpRectangle.getMinY(),
				this.tmpRectangle.getWidth(),
				this.tmpRectangle.getHeight(),
				ZoomableContextUtil.logical2pixel_size(arcWidth, this.zoomFactor),
				ZoomableContextUtil.logical2pixel_size(arcHeight, this.zoomFactor)) ;
		this.target.draw(r);
	}

	/** Fills an arc.
	 *  <p>
	 *  The given coordinates will be transformed.
	 *
	 * @param x x coordinate of the covered rectangle
	 * @param y y coordinate of the covered rectangle
	 * @param width the width of the covered rectangle
	 * @param height the height of the covered rectangle
	 * @param startAngle the starting angle
	 * @param arcAngle the size of the arc
	 */
	@Override
	public void fillArc( float x, float y, float width, float height, float startAngle, float arcAngle ) {
		this.tmpRectangle.setRect(x, y, width, height);
		ZoomableAwtContextUtil.logical2pixel(this.tmpRectangle,
				this.centeringTransform, this.zoomFactor);
		Arc2D a = new Arc2D.Double(
				this.tmpRectangle.getMinX(),
				this.tmpRectangle.getMinY(),
				this.tmpRectangle.getWidth(),
				this.tmpRectangle.getHeight(),
				startAngle, arcAngle,
				Arc2D.OPEN);
		this.target.fill(a);
	}

	/** fills an oval, using the current color, inside
	 *  the specified rectangle.
	 *  <p>
	 *  The given coordinates will be transformed.
	 *
	 * @param x horizontal location of the enclosing rectangle.
	 * @param y vertical location of the enclosing rectangle.
	 * @param w width of the enclosing rectangle.
	 * @param h height of the enclosing rectangle.
	 */
	@Override
	public void fillOval( float x, float y, float w, float h ) {
		this.tmpRectangle.setRect(x, y, w, h);
		ZoomableAwtContextUtil.logical2pixel(this.tmpRectangle,
				this.centeringTransform, this.zoomFactor);
		Ellipse2D oval = new Ellipse2D.Double(
				this.tmpRectangle.getMinX(),
				this.tmpRectangle.getMinY(),
				this.tmpRectangle.getWidth(),
				this.tmpRectangle.getHeight());
		this.target.fill(oval);
	}

	/** Fills a rectangle.
	 *  <p>
	 *  The given coordinates will be transformed.
	 *
	 * @param x horizontal location of the rectangle.
	 * @param y vertical location of the rectangle.
	 * @param w width of the rectangle.
	 * @param h height of the rectangle.
	 */
	@Override
	public void fillRect( float x, float y, float w, float h ) {
		this.tmpRectangle.setRect(x, y, w, h);
		ZoomableAwtContextUtil.logical2pixel(this.tmpRectangle,
				this.centeringTransform, this.zoomFactor);
		this.target.fill(this.tmpRectangle);
	}

	/** Draws a rectangle.
	 *  <p>
	 *  The given coordinates will be transformed.
	 *
	 * @param x horizontal location of the rectangle.
	 * @param y vertical location of the rectangle.
	 * @param w width of the rectangle.
	 * @param h height of the rectangle.
	 */
	@Override
	public final void drawRect( int x, int y, int w, int h ) {
		drawRect((float)x, (float)y, (float)w, (float)h);
	}

	/** Draws a rectangle.
	 *  <p>
	 *  The given coordinates will be transformed.
	 *
	 * @param x horizontal location of the rectangle.
	 * @param y vertical location of the rectangle.
	 * @param w width of the rectangle.
	 * @param h height of the rectangle.
	 */
	@Override
	public void drawRect( float x, float y, float w, float h ) {
		this.tmpRectangle.setRect(x, y, w, h);
		ZoomableAwtContextUtil.logical2pixel(this.tmpRectangle,
				this.centeringTransform, this.zoomFactor);
		this.target.draw(this.tmpRectangle);
	}

	/** Fills a rounded-rectangle.
	 *  <p>
	 *  The given coordinates will be transformed.
	 *
	 * @param x horizontal location of the rectangle.
	 * @param y vertical location of the rectangle.
	 * @param w width of the rectangle.
	 * @param h height of the rectangle.
	 * @param arcWidth width of the corners' angle.
	 * @param arcHeight height of the corners' angle.
	 */
	@Override
	public void fillRoundRect( float x, float y, float w, float h, float arcWidth, float arcHeight ) {
		this.tmpRectangle.setRect(x, y, w, h);
		ZoomableAwtContextUtil.logical2pixel(this.tmpRectangle,
				this.centeringTransform, this.zoomFactor);
		RoundRectangle2D r = new RoundRectangle2D.Double(
				this.tmpRectangle.getMinX(),
				this.tmpRectangle.getMinY(),
				this.tmpRectangle.getWidth(),
				this.tmpRectangle.getHeight(),
				ZoomableContextUtil.logical2pixel_size(arcWidth, this.zoomFactor),
				ZoomableContextUtil.logical2pixel_size(arcWidth, this.zoomFactor));
		this.target.fill(r);
	}

	/** Replies the current clipping area.
	 *
	 * @return the current clipping area.
	 */
	@Override
	public Shape getClip() {
		return pixel2logical( this.target.getClip() ) ;
	}

	/** Sets the current clipping area.
	 *
	 * @param x horizontal location of the rectangle.
	 * @param y vertical location of the rectangle.
	 * @param width is the width of the rectangle.
	 * @param height is the height of the rectangle.
	 */
	@Override
	public void setClip( float x, float y, float width, float height ) {
		this.tmpRectangle.setRect(x, y, width, height);
		ZoomableAwtContextUtil.logical2pixel(this.tmpRectangle,
				this.centeringTransform, this.zoomFactor);
		this.target.setClip((Shape)this.tmpRectangle.clone());
	}

	/** Sets the current clipping area.
	 *
	 * @param c the new clipping area that was copyed.
	 */
	@Override
	public void setClip( Shape c ) {
		this.target.setClip( logical2pixel( c ) ) ;
	}

	/** {@inheritDoc}
	 * @see #setFont(Font, boolean)
	 * @see #setPixelFont(Font)
	 */
	@Override
	public final void setFont( Font f ) {
		setFont(f, true);
	}

	/** Set the current font.
	 * 
	 * @param f
	 * @param enableScaling indicates if the scaling must be apply to the given font.
	 * @since 4.1
	 */
	protected void setFont(Font f, boolean enableScaling) {
		Font sf = f;
		if (enableScaling) {
			sf = ZoomableAwtContextUtil.logical2pixel(
					sf,
					this.zoomFactor);
		}
		this.target.setFont( sf ) ;
	}

	/** {@inheritDoc}
	 */
	@Override
	public FontMetrics getFontMetrics(Font font) {
		return this.target.getFontMetrics( font ) ;
	}

	/** {@inheritDoc}
	 * @see #getPixelFont()
	 */
	@Override
	public Font getFont() {
		Font font = this.target.getFont();
		font = ZoomableAwtContextUtil.pixel2logical(
				font,
				this.zoomFactor);
		font = new FontUIResource(font);
		return font;
	}

	/** {@inheritDoc}
	 */
	@Override
	public FontMetrics getFontMetrics() {
		return this.target.getFontMetrics(getFont());
	}

	////////////////////////////////////////////////////////////
	// Graphics2D API

	/** Intersects the current clip with the specified rectangle.
	 *  <p>
	 *  The given coordinates will <em>not</em> be transformed.
	 *
	 * @param area the clipping area used to intersect.
	 */
	@Override
	public void clip( Shape area ) {
		this.target.clip( logical2pixel( area ) );
	}

	/** {@inheritDoc}
	 */
	@Override
	protected void drawPoint(float x, float y, float pointSizeInPixel) {
		super.drawPoint(x, y, pixel2logical_size(pointSizeInPixel));
	}

	/** Strokes the outline of a specified shape.
	 *
	 * @param s the shape to draw.
	 */
	@Override
	public void draw( Shape s ) {
		this.target.draw( logical2pixel( s ) ) ;
	}

	@Override
	public void draw(PathIterator s) {
		this.target.draw( ZoomableAwtContextUtil.logical2pixel(
				s, this.centeringTransform, this.zoomFactor) );
	}

	/** Renders the text of the specified glyph.
	 *
	 * @param glyph the glyph to display.
	 * @param x horizontal location of the text.
	 * @param y vertical location of the text.
	 */
	@Override
	public void drawGlyphVector( GlyphVector glyph, float x, float y ) {
		if (glyph.getNumGlyphs()>0) {
			Rectangle2D bounds = glyph.getPixelBounds(glyph.getFontRenderContext(), x, y);
			this.tmpRectangle.setRect(bounds);
			ZoomableAwtContextUtil.logical2pixel(this.tmpRectangle,
					this.centeringTransform, this.zoomFactor);
			AffineTransform o = getTransform();
			this.target.scale(this.zoomFactor, this.zoomFactor) ;
			this.target.drawGlyphVector(glyph,
					(float)(this.tmpRectangle.getMinX() + this.tmpRectangle.getWidth()),
					(float)(this.tmpRectangle.getMinY() + this.tmpRectangle.getHeight())) ; 
			this.target.setTransform(o) ;
		}
	}

	/** Draws the specified image.
	 *
	 * @param img the image to draw.
	 * @param op the filter to be applied to the image before rendering.
	 * @param x horizontal location of the image.
	 * @param y vertical location of the image.
	 * @see #setImageOriginalSize(boolean)
	 * @see #isImageOriginalSize()
	 */
	@Override
	public void drawImage( BufferedImage img, BufferedImageOp op, float x, float y ) {
		BufferedImage drawableImage;

		if (this.scaleImage) {
			this.tmpRectangle.setRect(x, y, img.getWidth(), img.getHeight());
		}
		else {
			float sx = ZoomableContextUtil.pixel2logical_size(img.getWidth(), this.zoomFactor);
			float sy = ZoomableContextUtil.pixel2logical_size(img.getHeight(), this.zoomFactor);
			this.tmpRectangle.setRect(x, y, sx, sy);
		}

		ZoomableAwtContextUtil.logical2pixel(this.tmpRectangle, this.centeringTransform, this.zoomFactor);

		if (this.tmpRectangle.isEmpty()) return;

		if (this.scaleImage) {
			int sx = (int)this.tmpRectangle.getWidth();
			int sy = (int)this.tmpRectangle.getHeight();
			drawableImage = new BufferedImage(sx, sy, img.getType());
			Graphics2D g = (Graphics2D)drawableImage.getGraphics();
			g.drawImage(img, 0, 0, sx, sy, 0, 0, img.getWidth(), img.getHeight(), null);
			g.dispose();
		}
		else {
			drawableImage = img;
		}

		int ix = (int)this.tmpRectangle.getMinX();
		int iy = (int)this.tmpRectangle.getMinY();

		this.target.drawImage(drawableImage, op, ix, iy);
	}
	
	private AffineTransform toPixelTransform(AffineTransform trans) {
		AffineTransform mat;
		if (this.scaleImage)
			mat = ZoomableAwtContextUtil.getMatrix(this.centeringTransform, this.zoomFactor);
		else
			mat = ZoomableAwtContextUtil.getMatrixNoScaling(this.centeringTransform, this.zoomFactor);
			
		mat.concatenate(trans);

		return mat;
	}

	/** Draws the given image.
	 *
	 * @param img the image to draw.
	 * @param xform the affine transformation to apply.
	 * @param obs the notified observer.
	 * @return <code>true</code> if the image was completly rendered.
	 * @see #setImageOriginalSize(boolean)
	 * @see #isImageOriginalSize()
	 */
	@Override
	public boolean drawImage( Image img, AffineTransform xform, ImageObserver obs ) {
		return this.target.drawImage( img, toPixelTransform(xform), obs ) ;
	}
	
	/** Renders a RenderableImage.
	 *
	 * @param img the image to draw.
	 * @param xform the affine transformation to apply.
	 * @see #setImageOriginalSize(boolean)
	 * @see #isImageOriginalSize()
	 */
	@Override
	public void drawRenderableImage( RenderableImage img, AffineTransform xform ) {
		this.target.drawRenderableImage( img, toPixelTransform(xform) ) ;
	}

	/** Renders a RenderedImage.
	 *
	 * @param img the image to draw.
	 * @param xform the affine transformation to apply.
	 * @see #setImageOriginalSize(boolean)
	 * @see #isImageOriginalSize()
	 */
	@Override
	public void drawRenderedImage( RenderedImage img, AffineTransform xform ) {
		this.target.drawRenderedImage( img, toPixelTransform(xform) ) ;
	}

	/** Draws the given string.
	 *
	 * @param s the string to draw.
	 * @param x horizontal location of the string.
	 * @param y vertical location of the string.
	 */
	@Override
	public void drawString( AttributedCharacterIterator s, float x, float y ) {
		FontMetrics fm = getFontMetrics();
		Rectangle2D r = fm.getMaxCharBounds(this);
		this.tmpRectangle.setRect(x, y, r.getWidth()*(s.getEndIndex()-s.getBeginIndex()+1), fm.getHeight());
		int baseline = fm.getMaxDescent();
		ZoomableAwtContextUtil.logical2pixel(this.tmpRectangle,
				this.centeringTransform, this.zoomFactor);
		this.target.drawString(s,
				(float)this.tmpRectangle.getMinX(),
				(float)(this.tmpRectangle.getMaxY()-baseline)); 
	}

	/** Draws the given string.
	 *
	 * @param s the string to draw.
	 * @param x horizontal location of the string.
	 * @param y vertical location of the string.
	 */
	@Override
	public void drawString( String s, float x, float y ) {
		FontMetrics fm = getFontMetrics();
		this.tmpRectangle.setRect(x, y, fm.stringWidth(s), fm.getHeight());
		fm = getPixelFontMetrics();
		int baseline = fm.getMaxDescent();
		ZoomableAwtContextUtil.logical2pixel(this.tmpRectangle,
				this.centeringTransform, this.zoomFactor);
		this.target.drawString(s,
				(float)this.tmpRectangle.getMinX(),
				(float)(this.tmpRectangle.getMaxY()-baseline)); 
	}

	/** Fills the interior of a shape.
	 *
	 * @param s the shape to fill.
	 */
	@Override
	public void fill( Shape s ) {
		this.target.fill( logical2pixel( s ) ) ;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fill( PathIterator path ) {
		this.target.fill( ZoomableAwtContextUtil.logical2pixel( path,
				this.centeringTransform, this.zoomFactor) ) ;
	}

	/** Adds a rotation into the current
	 *  affine transformation.
	 *
	 * @param theta rotation angle
	 * @param rx horizontal translation
	 * @param ry vertical translation
	 */
	@Override
	public void rotate( float theta, float rx, float ry ) {
		this.target.rotate( theta, logical2pixel_x( rx ), logical2pixel_y( ry ) ) ;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotate( float theta ) {
		this.target.rotate( theta, logical2pixel_x( 0 ), logical2pixel_y( 0 ) ) ;
	}

	/** Replaces the current brush.
	 * <p>
	 * This function scales the specified stroke.
	 * To set the brush stroke without zoom factor,
	 * see {@link Graphics2D#setStroke(java.awt.Stroke)}.
	 * 
	 * @param stroke the new brush.
	 * @since 1.8
	 * @see Graphics2D#setStroke(java.awt.Stroke)
	 */
	public void setScaledStroke(Stroke stroke) {
		if (stroke instanceof BasicStroke) {
			BasicStroke basicStroke = (BasicStroke) stroke;
			BasicStroke scaledBasicStroke = new BasicStroke(
					logical2pixel_size(basicStroke.getLineWidth()),
					basicStroke.getEndCap(), basicStroke.getLineJoin(),
					basicStroke.getMiterLimit(), basicStroke.getDashArray(),
					basicStroke.getDashPhase());

			this.target.setStroke(scaledBasicStroke);
		}
		else {
			this.target.setStroke(stroke);
		}
	}

	/** Adds a translation into the current
	 *  affine transformation.
	 *
	 * @param tx horizontal translation.
	 * @param ty vertical translation
	 */
	@Override
	public void translate( float tx, float ty ) {
		this.target.translate( logical2pixel_size( tx ), logical2pixel_size( ty ) ) ;
	}

	//------------------------------------------------------
	// Getter/Setter
	//------------------------------------------------------

	/** Replies if the drawn images must be scaled or
	 * displayed with there original size in the screen space.
	 * <p>
	 * The not-scaled images will always be drawn at the
	 * geo-reference points.
	 * <p>
	 * Images are not icons. To retreive the similar flag for icons
	 * please use {@link #isIconOriginalSize()}.
	 * 
	 * @return <code>true</code> if the image must be drawn with
	 * its original size, otherwhise <code>false</code> to
	 * assume that each pixel of the bitmap is a square-meter area.
	 * @see #isIconOriginalSize()
	 * @see #drawImage(BufferedImage,BufferedImageOp,float,float)
	 * @see #drawImage(Image,AffineTransform,ImageObserver)
	 * @see #drawImage(Image,float,float,Color,ImageObserver)
	 * @see #drawImage(Image,float,float,float,float,Color,ImageObserver)
	 * @see #drawImage(Image,float,float,float,float,ImageObserver)
	 * @see #drawImage(Image,float,float,float,float,int,int,int,int,Color,ImageObserver)
	 * @see #drawImage(Image,float,float,float,float,int,int,int,int,ImageObserver)
	 * @see #drawImage(Image,float,float,ImageObserver)
	 * @see #drawRenderableImage(RenderableImage,AffineTransform)
	 * @see #drawRenderedImage(RenderedImage,AffineTransform)
	 */
	public boolean isImageOriginalSize() {
		return !this.scaleImage ;
	}

	/** Set if the drawn images must be scaled or
	 * displayed with there original size in the screen space.
	 * <p>
	 * The not-scaled images will always be drawn at the
	 * geo-reference points.
	 * <p>
	 * Images are not icons. To set the similar flag for icons
	 * please use {@link #setIconOriginalSize(boolean)}.
	 * 
	 * @param originalSize must be <code>true</code>
	 * if the image must be drawn with
	 * its original size, otherwhise <code>false</code> to
	 * assume that each pixel of the bitmap is a square-meter area.
	 * @see #setIconOriginalSize(boolean)
	 * @see #drawImage(BufferedImage,BufferedImageOp,float,float)
	 * @see #drawImage(Image,AffineTransform,ImageObserver)
	 * @see #drawImage(Image,float,float,Color,ImageObserver)
	 * @see #drawImage(Image,float,float,float,float,Color,ImageObserver)
	 * @see #drawImage(Image,float,float,float,float,ImageObserver)
	 * @see #drawImage(Image,float,float,float,float,int,int,int,int,Color,ImageObserver)
	 * @see #drawImage(Image,float,float,float,float,int,int,int,int,ImageObserver)
	 * @see #drawImage(Image,float,float,ImageObserver)
	 * @see #drawRenderableImage(RenderableImage,AffineTransform)
	 * @see #drawRenderedImage(RenderedImage,AffineTransform)
	 */
	public void setImageOriginalSize( boolean originalSize ) {
		this.scaleImage = !originalSize;
	}

	/** Replies if the drawn icons must be scaled or
	 * displayed with there original size in the screen space.
	 * <p>
	 * The not-scaled icons will always be drawn at the
	 * geo-reference points.
	 * <p>
	 * Icons are not images. To retreive the similar flag for images
	 * please use {@link #isImageOriginalSize()}.
	 * 
	 * @return <code>true</code> if the icon must be drawn with
	 * its original size, otherwhise <code>false</code> to
	 * assume that each pixel of the icon is a square-meter area.
	 * @see #isImageOriginalSize()
	 * @see #drawIcon(Icon, float, float)
	 * @see #drawIcon(Icon, int, int)
	 * @since 4.0
	 */
	public boolean isIconOriginalSize() {
		return !this.scaleIcon ;
	}

	/** Set if the drawn icons must be scaled or
	 * displayed with there original size in the screen space.
	 * <p>
	 * The not-scaled icon will always be drawn at the
	 * geo-reference points.
	 * <p>
	 * Icons are not images. To set the similar flag for images
	 * please use {@link #setImageOriginalSize(boolean)}.
	 * 
	 * @param originalSize must be <code>true</code>
	 * if the icon must be drawn with
	 * its original size, otherwhise <code>false</code> to
	 * assume that each pixel of the icon is a square-meter area.
	 * @see #setImageOriginalSize(boolean)
	 * @see #drawIcon(Icon, float, float)
	 * @see #drawIcon(Icon, int, int)
	 * @since 4.0
	 */
	public void setIconOriginalSize( boolean originalSize ) {
		this.scaleIcon = !originalSize;
	}

	/** Replies if the drawn icons must be centered on
	 * the given painting coordinates; or not.
	 * 
	 * @return <code>true</code> if the icon is
	 * centered when drawn, otherwhise <code>false</code>.
	 * @see #drawIcon(Icon, float, float)
	 * @see #drawIcon(Icon, int, int)
	 * @since 4.0
	 */
	public boolean isIconCentered() {
		return this.centerIcon ;
	}

	/** Set if the drawn icons must be centered on
	 * the given painting coordinates; or not.
	 * 
	 * @param isCentered is <code>true</code> if the icon is
	 * centered when drawn, otherwhise <code>false</code>.
	 * @see #drawIcon(Icon, float, float)
	 * @see #drawIcon(Icon, int, int)
	 * @since 4.0
	 */
	public void setIconCentered(boolean isCentered) {
		this.centerIcon = isCentered;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getScalingFactor() {
		return this.zoomFactor;
	}

	//------------------------------------------------------
	// Function releated to the change of coordinates
	//------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float logical2pixel_size(float l) {
		return ZoomableContextUtil.logical2pixel_size(l, this.zoomFactor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float logical2pixel_x(float l) {
		return ZoomableContextUtil.logical2pixel_x(l,
				this.centeringTransform, this.zoomFactor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float logical2pixel_y(float l) {
		return ZoomableContextUtil.logical2pixel_y(l,
				this.centeringTransform, this.zoomFactor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float pixel2logical_size(float l) {
		return ZoomableContextUtil.pixel2logical_size(l, this.zoomFactor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float pixel2logical_x(float p) {
		return ZoomableContextUtil.pixel2logical_x(p,
				this.centeringTransform, this.zoomFactor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float pixel2logical_y(float p) {
		return ZoomableContextUtil.pixel2logical_y(p,
				this.centeringTransform, this.zoomFactor);
	}

	/** Translates the specified point
	 *  into the screen space.
	 *
	 * @param s is the point in the logical space.
	 * @return a point into the screen space.
	 */
	public Point2D logical2pixel(Point2D s) {
		return new Point2D.Double(
				logical2pixel_x((float)s.getX()),
				logical2pixel_y((float)s.getY()));
	}

	/**
	 * Translate the given shape from the logical coordinate space
	 * to the screen coordinate space.
	 * 
	 * @param s
	 * @return the translation of s
	 */
	public <T extends Shape> T logical2pixel(T s) {
		return ZoomableAwtContextUtil.logical2pixel(
				s, this.centeringTransform, this.zoomFactor);
	}

	/**
	 * Translate the given font from the logical coordinate space
	 * to the screen coordinate space.
	 * 
	 * @param f
	 * @return the translation of f
	 */
	public Font logical2pixel(Font f) {
		return ZoomableAwtContextUtil.logical2pixel(f, this.zoomFactor);
	}

	/** Translates the specified point
	 *  into the logical space.
	 *
	 * @param s is the point in the screen space.
	 * @return a point into the logical space.
	 */
	public Point2D pixel2logical(Point2D s) {
		return new Point2D.Double(
				pixel2logical_x((int)s.getX()),
				pixel2logical_y((int)s.getY()));
	}

	/**
	 * Translate the given shape from the screen coordinate space
	 * to the logical coordinate space.
	 * 
	 * @param s
	 * @return the translation of s
	 */
	public <T extends Shape> T pixel2logical(T s) {
		return ZoomableAwtContextUtil.pixel2logical(
				s, this.centeringTransform, this.zoomFactor);
	}

	/**
	 * Translate the given font from the screen coordinate space
	 * to the logical coordinate space.
	 * 
	 * @param f
	 * @return the translation of f
	 */
	public Font pixel2logical(Font f) {
		return ZoomableAwtContextUtil.pixel2logical(f, this.zoomFactor);
	}

	@Override
	public PathIterator2f logical2pixel(PathIterator2f p) {
		return ZoomableContextUtil.logical2pixel(p, this.centeringTransform, this.zoomFactor);
	}

	@Override
	public PathIterator2f pixel2logical(PathIterator2f p) {
		return ZoomableContextUtil.pixel2logical(p, this.centeringTransform, this.zoomFactor);
	}

	@Override
	public void logical2pixel(Segment2f s) {
		ZoomableContextUtil.logical2pixel(s, this.centeringTransform, this.zoomFactor);
	}

	@Override
	public void pixel2logical(Segment2f s) {
		ZoomableContextUtil.pixel2logical(s, this.centeringTransform, this.zoomFactor);
	}

	@Override
	public void logical2pixel(RoundRectangle2f r) {
		ZoomableContextUtil.logical2pixel(r, this.centeringTransform, this.zoomFactor);
	}

	@Override
	public void pixel2logical(RoundRectangle2f r) {
		ZoomableContextUtil.pixel2logical(r, this.centeringTransform, this.zoomFactor);
	}

	@Override
	public void logical2pixel(Point2f p) {
		ZoomableContextUtil.logical2pixel(p, this.centeringTransform, this.zoomFactor);
	}

	@Override
	public void pixel2logical(Point2f p) {
		ZoomableContextUtil.pixel2logical(p, this.centeringTransform, this.zoomFactor);
	}

	@Override
	public void logical2pixel(Ellipse2f e) {
		ZoomableContextUtil.logical2pixel(e, this.centeringTransform, this.zoomFactor);
	}

	@Override
	public void pixel2logical(Ellipse2f e) {
		ZoomableContextUtil.pixel2logical(e, this.centeringTransform, this.zoomFactor);
	}

	@Override
	public void logical2pixel(Circle2f r) {
		ZoomableContextUtil.logical2pixel(r, this.centeringTransform, this.zoomFactor);
	}

	@Override
	public void pixel2logical(Circle2f r) {
		ZoomableContextUtil.pixel2logical(r, this.centeringTransform, this.zoomFactor);
	}

	@Override
	public void logical2pixel(Rectangle2f r) {
		ZoomableContextUtil.logical2pixel(r, this.centeringTransform, this.zoomFactor);
	}

	@Override
	public void pixel2logical(Rectangle2f r) {
		ZoomableContextUtil.pixel2logical(r, this.centeringTransform, this.zoomFactor);
	}

	@Override
	public Shape2f logical2pixel(Shape2f s) {
		return ZoomableContextUtil.logical2pixel(s, this.centeringTransform, this.zoomFactor);
	}

	@Override
	public Shape2f pixel2logical(Shape2f s) {
		return ZoomableContextUtil.pixel2logical(s, this.centeringTransform, this.zoomFactor);
	}

	/**
	 * Return the rectangle that corresponds to the displayed
	 * area.
	 * 
	 * @return the rectangle expressed in the logic
	 * coordinate space; or <code>null</code>
	 * @see #getTargetComponentSize()
	 * @see #getViewportSize()
	 */
	@Override
	public Rectangle2D getViewportRect() {
		Component c = (this.targetComponent==null) ? null : 
			this.targetComponent.get();
		if (c==null) return null;
		Dimension2D d = c.getSize();
		if (d==null) return null;
		Rectangle2D r = new Rectangle2D.Double(
				0, 0, d.getWidth()-1, d.getHeight()-1);
		ZoomableAwtContextUtil.pixel2logical(
				r,
				this.centeringTransform, this.zoomFactor);
		return r;
	}

	/**
	 * Return the size of the viewport in the logical space.
	 * 
	 * @return a dimension or <code>null</code>
	 * @see #getTargetComponentSize()
	 * @see #getViewportRect()
	 */
	@Override
	public Dimension2D getViewportSize() {
		Component c = (this.targetComponent==null) ? null : 
			this.targetComponent.get();
		if (c==null) return null;
		Dimension2D d = c.getSize();
		if (d!=null) {
			return new DoubleDimension(
					ZoomableContextUtil.pixel2logical_size(
							(float)d.getWidth(), this.zoomFactor),
							ZoomableContextUtil.pixel2logical_size(
									(float)d.getHeight(), this.zoomFactor));
		}
		return d;
	}

	/** Replies the current zooming context.
	 * 
	 * @return the current zooming context.
	 */
	public ZoomableContext getZoomableContext() {
		return this;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void drawCartridge(String text, float x, float y, Color textColor, Color backgroundColor, Color borderColor, TextAlignment alignment) {
		String[] lines = text.split("\n"); //$NON-NLS-1$
		float margin = (backgroundColor!=null || borderColor!=null) ? CARTRIDGE_MARGIN : 0;
		Rectangle2D cartidgeBounds = new Rectangle2D.Double();
		float px, py;
		px = ZoomableContextUtil.logical2pixel_x(
				x, this.centeringTransform, this.zoomFactor);
		py = ZoomableContextUtil.logical2pixel_y(
				y, this.centeringTransform, this.zoomFactor);
		// Force font metric to not be scaled
		Font oldFont = getFont();
		Font unscaledFont = oldFont.deriveFont(this.defaultFontSize);
		setFont(unscaledFont, false);
		// Compute cartridge
		cartridgeBounds(lines, px, py, margin, cartidgeBounds);

		drawCartridge(lines, cartidgeBounds, margin, textColor, backgroundColor, borderColor, alignment);
		setFont(oldFont, false);
	}

	/** Replies the unscaled font.
	 * 
	 * @return the unscaled font.
	 */
	public Font getOriginalUnscaledFont() {
		Font oldFont = getFont();
		return oldFont.deriveFont(this.defaultFontSize);
	}

	/**
	 * Gets the font metrics of the current font inside
	 * the <strong>pixel workspace</strong>.
	 * <p>
	 * The replied font metrics ignores any transformation
	 * previously applied to the graphic context such
	 * as zooming or translation. It means that any dimension
	 * and size in the FontMetrics are screen pixels.
	 * 
	 * @return    the font metrics of this graphics 
	 *                    context's current font.
	 * @see #getFontMetrics() for the logical workspace version.
	 * @since 4.0
	 */
	public FontMetrics getPixelFontMetrics() {
		return this.target.getFontMetrics();
	}

	/**
	 * Gets the font metrics of the given font inside
	 * the <strong>pixel workspace</strong>.
	 * <p>
	 * The replied font metrics ignores any transformation
	 * previously applied to the graphic context such
	 * as zooming or translation. It means that any dimension
	 * and size in the FontMetrics are screen pixels.
	 * 
	 * @param f
	 * @return    the font metrics of this graphics 
	 *                    context's current font.
	 * @see #getFontMetrics(Font) for the logical workspace version.
	 * @since 4.0
	 */
	public FontMetrics getPixelFontMetrics(Font f) {
		return this.target.getFontMetrics(f);
	}

	/**
	 * Gets the current font in the <strong>pixel workspace</strong>.
	 * <p>
	 * The replied font ignores any transformation
	 * previously applied to the graphic context such
	 * as zooming or translation. It means that any dimension
	 * and size in the Font are screen pixels.
	 * 
	 * @return    this graphics context's current font.
	 * @see #getFont() for the logical workspace version.
	 * @since 4.0
	 */
	public Font getPixelFont() {
		return this.target.getFont();
	}

	/**
	 * Sets this graphics context's font to the specified font
	 * in <strong>pixel workspace</strong>.
	 * <p>
	 * The font ignores any transformation
	 * previously applied to the graphic context such
	 * as zooming or translation. It means that any dimension
	 * and size in the Font are screen pixels.
	 * <p> 
	 * All subsequent text operations using this graphics context 
	 * use this font. A null argument is silently ignored.
	 * 
	 * @param  font   the font.
	 * @see #setFont(Font) for the logical workspace version.
	 * @since 4.0
	 */
	public void setPixelFont(Font font) {
		this.target.setFont(font);
	}

	/**
	 * Renders in the <strong>pixel workspace</strong> the text specified by the specified <code>String</code>, 
	 * using the current text attribute state in the <code>Graphics2D</code> context.
	 * <p>
	 * This functions ignores any transformation
	 * previously applied to the graphic context such
	 * as zooming or translation. It means that the string 
	 * will be rendered according to the FontMetrics replied
	 * by {@link #getPixelFontMetrics()} and at the screen pixel
	 * {@code (x,y)}.
	 * <p>
	 * The baseline of the first character is at position 
	 * (<i>x</i>,&nbsp;<i>y</i>) in the User Space.
	 * The rendering attributes applied include the <code>Clip</code>,
	 * <code>Transform</code>, <code>Paint</code>, <code>Font</code> and
	 * <code>Composite</code> attributes. For characters in script systems 
	 * such as Hebrew and Arabic, the glyphs can be rendered from right to
	 * left, in which case the coordinate supplied is the location of the
	 * leftmost character on the baseline.
	 * @param str the <code>String</code> to be rendered
	 * @param x the x coordinate of the location where the
	 * <code>String</code> should be rendered
	 * @param y the y coordinate of the location where the
	 * <code>String</code> should be rendered
	 * @throws NullPointerException if <code>str</code> is
	 *         <code>null</code>
	 * @see #drawString(String,float,float) for the logical workspace version.
	 * @since 4.0
	 */
	public void drawPixelString(String str, float x, float y) {
		this.target.drawString(str, x, y);
	}

	/**
	 * Renders in the <strong>pixel workspace</strong> the text specified by the specified <code>String</code>, 
	 * using the current text attribute state in the <code>Graphics2D</code> context.
	 * <p>
	 * This functions ignores any transformation
	 * previously applied to the graphic context such
	 * as zooming or translation. It means that the string 
	 * will be rendered according to the FontMetrics replied
	 * by {@link #getPixelFontMetrics()} and at the screen pixel
	 * {@code (x,y)}.
	 * <p>
	 * The baseline of the first character is at position 
	 * (<i>x</i>,&nbsp;<i>y</i>) in the User Space.
	 * The rendering attributes applied include the <code>Clip</code>,
	 * <code>Transform</code>, <code>Paint</code>, <code>Font</code> and
	 * <code>Composite</code> attributes. For characters in script systems 
	 * such as Hebrew and Arabic, the glyphs can be rendered from right to
	 * left, in which case the coordinate supplied is the location of the
	 * leftmost character on the baseline.
	 * @param str the <code>String</code> to be rendered
	 * @param x the x coordinate of the location where the
	 * <code>String</code> should be rendered
	 * @param y the y coordinate of the location where the
	 * <code>String</code> should be rendered
	 * @throws NullPointerException if <code>str</code> is
	 *         <code>null</code>
	 * @see #drawString(String,int,int) for the logical workspace version.
	 * @since 4.0
	 */
	public void drawPixelString(String str, int x, int y) {
		this.target.drawString(str, x, y);
	}

	/**
	 * Renders in the <strong>pixel workspace</strong> the text of the specified iterator applying its attributes
	 * in accordance with the specification of the {@link TextAttribute} class.
	 * <p>
	 * This functions ignores any transformation
	 * previously applied to the graphic context such
	 * as zooming or translation. It means that the string 
	 * will be rendered according to the FontMetrics replied
	 * by {@link #getPixelFontMetrics()} and at the screen pixel
	 * {@code (x,y)}.
	 * <p>
	 * The baseline of the first character is at position
	 * (<i>x</i>,&nbsp;<i>y</i>) in User Space.
	 * For characters in script systems such as Hebrew and Arabic,
	 * the glyphs can be rendered from right to left, in which case the 
	 * coordinate supplied is the location of the leftmost character
	 * on the baseline.
	 * @param iterator the iterator whose text is to be rendered
	 * @param x the x coordinate where the iterator's text is to be
	 * rendered
	 * @param y the y coordinate where the iterator's text is to be
	 * rendered
	 * @throws NullPointerException if <code>iterator</code> is
	 *         <code>null</code>
	 * @see #drawString(AttributedCharacterIterator, float, float)
	 * @since 4.0
	 */
	public void drawPixelString(AttributedCharacterIterator iterator, float x, float y) {
		this.target.drawString(iterator, x, y);
	}

	/**
	 * Renders in the <strong>pixel workspace</strong> the text of the specified iterator applying its attributes
	 * in accordance with the specification of the {@link TextAttribute} class.
	 * <p>
	 * This functions ignores any transformation
	 * previously applied to the graphic context such
	 * as zooming or translation. It means that the string 
	 * will be rendered according to the FontMetrics replied
	 * by {@link #getPixelFontMetrics()} and at the screen pixel
	 * {@code (x,y)}.
	 * <p>
	 * The baseline of the first character is at position
	 * (<i>x</i>,&nbsp;<i>y</i>) in User Space.
	 * For characters in script systems such as Hebrew and Arabic,
	 * the glyphs can be rendered from right to left, in which case the 
	 * coordinate supplied is the location of the leftmost character
	 * on the baseline.
	 * @param iterator the iterator whose text is to be rendered
	 * @param x the x coordinate where the iterator's text is to be
	 * rendered
	 * @param y the y coordinate where the iterator's text is to be
	 * rendered
	 * @throws NullPointerException if <code>iterator</code> is
	 *         <code>null</code>
	 * @see #drawString(AttributedCharacterIterator, int, int)
	 * @since 4.0
	 */
	public void drawPixelString(AttributedCharacterIterator iterator, int x, int y) {
		this.target.drawString(iterator, x, y);
	}

	/**
	 * Renders in the <strong>pixel workspace</strong> the text of the specified 
	 * {@link GlyphVector} using
	 * the <code>Graphics2D</code> context's rendering attributes.
	 * <p>
	 * This functions ignores any transformation
	 * previously applied to the graphic context such
	 * as zooming or translation. It means that the string 
	 * will be rendered according to the FontMetrics replied
	 * by {@link #getPixelFontMetrics()} and at the screen pixel
	 * {@code (x,y)}.
	 * <p>
	 * The rendering attributes applied include the <code>Clip</code>,
	 * <code>Transform</code>, <code>Paint</code>, and
	 * <code>Composite</code> attributes.  The <code>GlyphVector</code>
	 * specifies individual glyphs from a {@link Font}.
	 * The <code>GlyphVector</code> can also contain the glyph positions.  
	 * This is the fastest way to render a set of characters to the
	 * screen.
	 * @param g the <code>GlyphVector</code> to be rendered
	 * @param x the x position in User Space where the glyphs should
	 * be rendered
	 * @param y the y position in User Space where the glyphs should
	 * be rendered
	 * @throws NullPointerException if <code>g</code> is <code>null</code>.
	 * @see #drawGlyphVector(GlyphVector, float, float)
	 * @since 4.0
	 */
	public void drawPixelGlyphVector(GlyphVector g, float x, float y) {
		this.target.drawGlyphVector(g, x, y);
	}

	@Override
	public float getMaxScalingFactor() {
		return this.maxScalingFactor;
	}

	@Override
	public float getMinScalingFactor() {
		return this.minScalingFactor;
	}

	@Override
	public float getScalingSensitivity() {
		return this.scalingSensitivity;
	}

	@Override
	public float getFocusX() {
		return this.focusX;
	}

	@Override
	public float getFocusY() {
		return this.focusY;
	}

	@Override
	public boolean isXAxisInverted() {
		return this.centeringTransform.isXAxisFlipped();
	}

	@Override
	public boolean isYAxisInverted() {
		return this.centeringTransform.isYAxisFlipped();
	}

}
