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

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.generic.Vector2D;
import org.arakhne.afc.ui.MouseCursor;

/** Swing-oriented utilities. 
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public class AwtUtil {

	private static FontRenderContext frc =  null;

	/** Replies the AWT identifier of the mouse cursor.
	 * 
	 * @param cursor
	 * @return the AWT identifier of the mouse cursor.
	 */
	public static int getAWTCursorId(MouseCursor cursor) {
		switch(cursor) {
		case CROSSHAIR:
			return Cursor.CROSSHAIR_CURSOR;
		case TEXT:
			return Cursor.TEXT_CURSOR;
		case WAIT:
			return Cursor.WAIT_CURSOR;
		case SW_RESIZE:
			return Cursor.SW_RESIZE_CURSOR;
		case SE_RESIZE:
			return Cursor.SE_RESIZE_CURSOR;
		case NW_RESIZE:
			return Cursor.NW_RESIZE_CURSOR;
		case NE_RESIZE:
			return Cursor.NE_RESIZE_CURSOR;
		case N_RESIZE:
			return Cursor.N_RESIZE_CURSOR;
		case S_RESIZE:
			return Cursor.S_RESIZE_CURSOR;
		case W_RESIZE:
			return Cursor.W_RESIZE_CURSOR;
		case E_RESIZE:
			return Cursor.E_RESIZE_CURSOR;
		case HAND:
			return Cursor.HAND_CURSOR;
		case MOVE:
			return Cursor.MOVE_CURSOR;
		case INVALID:
			return Cursor.CUSTOM_CURSOR;
		case DEFAULT:
		default:
		}
		return Cursor.DEFAULT_CURSOR;
	}

	/** Replies the AWT mouse cursor.
	 * 
	 * @param cursor
	 * @return the AWT mouse cursor.
	 */
	public static Cursor getCursor(MouseCursor cursor) {
		if (cursor==null || cursor==MouseCursor.DEFAULT)
			return Cursor.getDefaultCursor();
		if (cursor==MouseCursor.INVALID) {
			try {
				return Cursor.getSystemCustomCursor("Invalid.32x32");  //$NON-NLS-1$
			}
			catch (Throwable exception) {
				return Cursor.getDefaultCursor();
			}
		}
		return Cursor.getPredefinedCursor(getAWTCursorId(cursor));
	}

	/** Compute and reply a transparent version of the given color.
	 * 
	 * @param c
	 * @return the transparent color.
	 */
	public static Color makeTransparentColor(Color c) {
		int alpha = c.getAlpha() / 2;
		return new Color(
				c.getRed(), c.getGreen(), c.getBlue(),
				alpha);
	}

	/** Replies the current font render context.
	 * 
	 * @return the current font render context.
	 */
	public static FontRenderContext getVectorFontRenderContext() {
		synchronized(AwtUtil.class) {
			FontRenderContext c = frc;
			if (c==null) {
				c = new FontRenderContext(new AffineTransform(), true, true);
				frc = c;
			}
			return c;
		}
	}

	/** Move the first specified rectangle to avoid collision 
	 * with the reference.
	 * 
	 * @param rectangleToMove is the rectangle to move.
	 * @param reference is the rectangle to avoid collision with.
	 * @return the displacement vector.
	 */
	public static Vector2D avoidCollision(Rectangle2D rectangleToMove, Rectangle2D reference) {
		double dx1 = reference.getMaxX() - rectangleToMove.getMinX();
		double dx2 = rectangleToMove.getMaxX() - reference.getMinX();
		double dy1 = reference.getMaxY() - rectangleToMove.getMinY();
		double dy2 = rectangleToMove.getMaxY() - reference.getMinY();

		double absdx1 = Math.abs(dx1);
		double absdx2 = Math.abs(dx2);
		double absdy1 = Math.abs(dy1);
		double absdy2 = Math.abs(dy2);

		double dx = 0;
		double dy = 0;

		if (dx1>=0 && absdx1<=absdx2 && absdx1<=absdy1 && absdx1<=absdy2) {
			// Move according to dx1
			dx = dx1; 
		}
		else if (dx2>=0 && absdx2<=absdx1 && absdx2<=absdy1 && absdx2<=absdy2) {
			// Move according to dx2
			dx = - dx2;
		}
		else if (dy1>=0 && absdy1<=absdx1 && absdy1<=absdx2 && absdy1<=absdy2) {
			// Move according to dy1
			dy = dy1; 
		}
		else {
			// Move according to dy2
			dy = - dy2;
		}

		rectangleToMove.setFrame(
				rectangleToMove.getMinX()+dx,
				rectangleToMove.getMinY()+dy,
				rectangleToMove.getWidth(),
				rectangleToMove.getHeight());

		return new Vector2f((float)dx, (float)dy);
	}

	/** Move the first specified rectangle to avoid collision 
	 * with the reference.
	 * 
	 * @param rectangleToMove is the rectangle to move.
	 * @param reference is the rectangle to avoid collision with.
	 * @param displacementDirection is the direction of the allowed displacement.
	 * @return the displacement vector.
	 */
	public static Vector2D avoidCollision(Rectangle2D rectangleToMove, Rectangle2D reference, Vector2D displacementDirection) {
		if (displacementDirection==null || displacementDirection.lengthSquared()==0f)
			return avoidCollision(rectangleToMove, reference);

		double dx1 = reference.getMaxX() - rectangleToMove.getMinX();
		double dx2 = reference.getMinX() - rectangleToMove.getMaxX();
		double dy1 = reference.getMaxY() - rectangleToMove.getMinY();
		double dy2 = reference.getMinY() - rectangleToMove.getMaxY();

		double absdx1 = Math.abs(dx1);
		double absdx2 = Math.abs(dx2);
		double absdy1 = Math.abs(dy1);
		double absdy2 = Math.abs(dy2);

		double dx, dy;

		if (displacementDirection.getX()<0) {
			dx = -Math.min(absdx1, absdx2);
		}
		else {
			dx = Math.min(absdx1, absdx2);
		}

		if (displacementDirection.getY()<0) {
			dy = -Math.min(absdy1, absdy2);
		}
		else {
			dy = Math.min(absdy1, absdy2);
		}

		rectangleToMove.setFrame(
				rectangleToMove.getMinX()+dx,
				rectangleToMove.getMinY()+dy,
				rectangleToMove.getWidth(),
				rectangleToMove.getHeight());

		displacementDirection.set((float)dx, (float)dy);
		return displacementDirection;
	}

	/** Replies the specified image filtered with the given transparency amount.
	 * <p>
	 * If this function replies <code>null</code>, you might invokes
	 * {@link #getTransparencyFilteredImage(Image, float)}
	 * with a not-<code>null</code> component parameter to obtain an
	 * image.
	 * 
	 * @param image is the image to filter.
	 * @param alpha indicates how the alpha-component of the image is changed.
	 * The value is in <code>[-1;1]</code>.
	 * A value of <code>-1</code> means that the alpha-component is set to none.
	 * A value of <code>1</code> means that the alpha-component is completely
	 * set. A value of <code>0</code> means that the alpha-component
	 * remains the same. 
	 * @return the filtered image or <code>null</code>.
	 */
	public static Image getTransparencyFilteredImage(Image image, float alpha) {
		if (image==null) return null;
		return TransparencyFilter.createFilteredImage(
				image,
				alpha);
	}

	/** Scale the given image.
	 * 
	 * @param image is the image to scale.
	 * @param width is the new width.
	 * @param height is the new height.
	 * @param observer is invoked when the image becomes ready to use.
	 * @return the scaled image; or <code>null</code> if the image is not ready or
	 * not computable.
	 */
	public static Image computeScaledImage(ImageIcon image, int width, int height, ImageObserver observer) {
		if (image==null) return null;
		Image imageToScale = extractIconImage(null, image);
		if (imageToScale==null) return null;
		int currentWidth = imageToScale.getWidth(observer);
		if (currentWidth<0) return null;
		int currentHeight = imageToScale.getHeight(observer);
		if (currentHeight<0) return null;
		Dimension2D d = computeBothScaledSize(
				currentWidth, currentHeight, width, height);
		if (d==null) return imageToScale; // Scaling not necessary
		return imageToScale.getScaledInstance(
				(int)d.getWidth(),(int)d.getHeight(),Image.SCALE_SMOOTH);
	}

	/** Extract an image from the specified icon.
	 * 
	 * @param component
	 * @param icon
	 * @return the image.
	 */
	public static Image extractIconImage(Component component, Icon icon) {
		Image source;
		if (icon instanceof ImageIcon) {
			source = ((ImageIcon)icon).getImage();
		}
		else if (component!=null) {
			source = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D)source.getGraphics();
			icon.paintIcon(component, g, 0, 0);
			g.dispose();
		}
		else {
			source = null;
		}
		return source;
	}

	/** Scale the given image.
	 * 
	 * @param image is the image to scale.
	 * @param width is the new width.
	 * @param height is the new height.
	 * @param observer is notified when the image cannot be loaded.
	 * @return the scaled image; or <code>null</code> if the image cannot be loaded.
	 */
	public static Image computeScaledImage(Image image, int width, int height, ImageObserver observer) {
		if (image==null) return null;
		int currentWidth = image.getWidth(observer);
		if (currentWidth<0) return null;
		int currentHeight = image.getHeight(observer);
		if (currentHeight<0) return null;
		Dimension2D d = computeBothScaledSize(
				currentWidth, currentHeight, 
				width, height);
		if (d==null) return image; // scaling is not necessary
		return image.getScaledInstance(
				(int)d.getWidth(),(int)d.getHeight(),Image.SCALE_SMOOTH);
	}

	private static Dimension2D computeBothScaledSize(int currentWidth, int currentHeight, int desiredWidth, int desiredHeight) {
		int targetWidth = desiredWidth;
		int targetHeight = desiredHeight;

		if (targetWidth==-1) targetWidth = currentWidth;
		if (targetHeight==-1) targetHeight = currentHeight;

		if (targetWidth==currentWidth && targetHeight==currentHeight)
			return null;

		float wscale = (float)targetWidth / (float)currentWidth; 
		float hscale = (float)targetHeight / (float)currentHeight;
		if (hscale<wscale) {
			int nwidth = (int)(currentWidth * hscale);
			if (nwidth>targetWidth) targetHeight = -1;
			else targetWidth = -1;					
		}
		else {
			int nheight = (int)(currentHeight * wscale);
			if (nheight>targetHeight) targetWidth = -1;
			else targetHeight = -1;					
		}
		return new DoubleDimension(targetWidth,targetHeight);
	}

	/** This class permits to filter the alpha component of an icon.
	 * 
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class TransparencyFilter extends RGBImageFilter {

		private final float alpha;

		/**
		 * Creates a alpha-filtered image
		 * 
		 * @param i is the image to filter
		 * @param alpha indicates how the alpha-component of the icon is changed.
		 * The value is in <code>[-1;1]</code>.
		 * A value of <code>-1</code> means that the alpha-component is set to none.
		 * A value of <code>1</code> means that the alpha-component is completely
		 * set. A value of <code>0</code> means that the alpha-component
		 * remains the same. 
		 * @return the result of the filtering.
		 */
		public static Image createFilteredImage(Image i, float alpha) {
			TransparencyFilter filter = new TransparencyFilter(alpha);
			ImageProducer prod = new FilteredImageSource(i.getSource(), filter);
			Image filteredImage = Toolkit.getDefaultToolkit().createImage(prod);
			return filteredImage;
		}

		/**
		 * Constructs an TransparencyFilter object that filters a color image to a 
		 * alphaed image. 
		 *
		 * @param alpha indicates how the alpha-component of the icon is changed.
		 * The value is in <code>[-1;1]</code>.
		 * A value of <code>-1</code> means that the alpha-component is set to none.
		 * A value of <code>1</code> means that the alpha-component is completely
		 * set. A value of <code>0</code> means that the alpha-component
		 * remains the same. 
		 */
		public TransparencyFilter(float alpha) {
			float f = alpha;
			if (f<-1f) f = -1f;
			else if (f>1f) f = 1f;
			assert(f>=-1f && f<=1f);
			this.alpha = -f;

			// canFilterIndexColorModel indicates whether or not it is acceptable
			// to apply the color filtering of the filterRGB method to the color
			// table entries of an IndexColorModel object in lieu of pixel by pixel
			// filtering.
			this.canFilterIndexColorModel = true;
		}

		/**
		 * Filter the specified color.
		 */
		@Override
		public int filterRGB(int x, int y, int rgb) {
			int color_a = (rgb >> 24) & 0xFF; 
			int color_rgb = rgb & 0x00FFFFFF; 

			if (this.alpha<0f) {
				color_a += color_a * this.alpha;
			}
			else {
				color_a += (256-color_a) * this.alpha;
			}

			if (color_a<0) color_a = 0;
			else if (color_a>255) color_a = 255;

			return ((color_a << 24) & 0xFF000000) | color_rgb;
		}

	} // class AlphaFilter

}
