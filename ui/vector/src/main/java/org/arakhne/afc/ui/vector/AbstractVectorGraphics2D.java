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

package org.arakhne.afc.ui.vector;

import java.lang.ref.SoftReference;
import java.net.URL;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.math.continous.object2d.Shape2f;
import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.ui.Graphics2DLOD;
import org.arakhne.afc.ui.TextAlignment;
import org.arakhne.afc.vmutil.Resources;

/** This graphic context permits to display
 *  something with a level of details.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractVectorGraphics2D implements VectorGraphics2D {

	private boolean isInteriorPainted = true;
	private boolean isOutlinePainted = true;
	private String interiorText = null;
	private Color outlineColor = null;
	private Color fillColor = null;
	private Paint paint = null;

	private static SoftReference<Image> noPictureBuffer = null;
	private static SoftReference<Image> noTransparentPictureBuffer = null;

	/**
	 */
	public AbstractVectorGraphics2D() {
		//
	}

	private static Image getNoPicture() {
		Image noPictureImage = noPictureBuffer==null ? null : noPictureBuffer.get();
		if (noPictureImage==null) {
			URL url = Resources.getResource(AbstractVectorGraphics2D.class, "no_picture.png"); //$NON-NLS-1$
			if (url!=null) {
				try {
					noPictureImage = VectorToolkit.image(url);
					noPictureBuffer = new SoftReference<Image>(noPictureImage);
				}
				catch(Throwable _) {
					noPictureImage = null;
					noPictureBuffer = null;
				}
			}
		}
		return noPictureImage;
	}

	private static Image getTransparentNoPicture() {
		Image noPictureImage = noTransparentPictureBuffer==null ? null : noTransparentPictureBuffer.get();
		if (noPictureImage==null) {
			Image originalImage = getNoPicture();
			if (originalImage!=null) {
				noPictureImage = VectorToolkit.image(originalImage, -.5f);
				noTransparentPictureBuffer = new SoftReference<Image>(noPictureImage);
			}
		}
		return noPictureImage;
	}

	@Override
	public final void drawDefaultImage(float dx1, float dy1, float dx2, float dy2) {
		Image noPictureImage;
		if (getLOD()==Graphics2DLOD.SHADOW) {
			noPictureImage = getTransparentNoPicture();
		}
		else {
			noPictureImage = getNoPicture();
		}

		if (noPictureImage!=null) {
			drawImage(
					null,
					noPictureImage,
					dx1, dy1, dx2, dy2,
					0, 0, noPictureImage.getWidth(null), noPictureImage.getHeight(null),
					null);
		}
		else {
			setInteriorPainted(true);
			Rectangle2f bounds = new Rectangle2f();
			bounds.setFromCorners(dx1, dy1, dx2, dy2);
			draw(bounds);
		}
	}

	/**
	 * @param fillColor is the initial filling color.
	 * @param outlineColor is the initial outline color.
	 * @param paint is the initial painting object.
	 * @param isInteriorPainted is the initial interior painting flag.
	 * @param isOutlinePainted is the initial outline painting flag.
	 * @param interiorText is the initial text inside a shape.
	 */
	public AbstractVectorGraphics2D(Color fillColor, Color outlineColor, Paint paint, boolean isInteriorPainted, boolean isOutlinePainted, String interiorText) {
		this.fillColor = fillColor;
		this.outlineColor = outlineColor;
		this.paint = paint;
		this.isInteriorPainted = isInteriorPainted;
		this.isOutlinePainted = isOutlinePainted;
		this.interiorText = interiorText;
	}

	@Override
	public void dispose() {
		this.isInteriorPainted = true;
		this.isOutlinePainted = true;
		this.interiorText = null;
		this.outlineColor = this.fillColor = null;
		this.paint = null;
	}

	@Override
	public void reset() {
		this.isInteriorPainted = true;
		this.isOutlinePainted = true;
		this.interiorText = null;
		this.outlineColor = this.fillColor = null;
		this.paint = null;
	}

	/** Log the given message.
	 * 
	 * @param message is the message to log.
	 */
	protected void log(String message) {
		System.out.println(toString()+": "+message); //$NON-NLS-1$
	}

	/** Log the given message.
	 * 
	 * @param message is the message to log out.
	 * @param exception is the exception to log out.
	 */
	protected void log(String message, Throwable exception) {
		String tag = toString();
		System.out.println(tag+": "+message); //$NON-NLS-1$
		System.out.println(tag+": "+exception.getLocalizedMessage()); //$NON-NLS-1$
		exception.printStackTrace();
	}

	/** Clean the state machine prior to any drawing.
	 */
	protected void preDrawing() {
		//
	}

	/** Clean the state machine after any drawing.
	 */
	protected void postDrawing() {
		this.interiorText = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Font getDefaultFont() {
		return VectorToolkit.font();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Paint setPaint(Paint paint) {
		Paint o = this.paint;
		this.paint = paint;
		return o;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Color setFillColor(Color color) {
		Color o = this.fillColor;
		this.fillColor = color;
		return o;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Color setOutlineColor(Color color) {
		Color o = this.outlineColor;
		this.outlineColor = color;
		return o;
	}

	@Override
	public void setColors(Color fillingColor, Color outlineColor) {
		if (fillingColor!=null) {
			setFillColor(fillingColor);
			setInteriorPainted(true);
		}
		else {
			setInteriorPainted(false);
		}
		if (outlineColor!=null) {
			setOutlineColor(fillingColor);
			setOutlineDrawn(true);
		}
		else {
			setOutlineDrawn(false);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Color getFillColor() {
		return this.fillColor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Color getOutlineColor() {
		return this.outlineColor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Paint getPaint() {
		return this.paint;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInteriorPainted() {
		return this.isInteriorPainted;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInteriorPainted(boolean painted) {
		this.isInteriorPainted = painted;
	}

	@Override
	public Point2D computeTextPosition(String text, Rectangle2f bounds,
			TextAlignment halign, TextAlignment valign) {
		FontMetrics fm = getFontMetrics();
		float width = fm.stringWidth(text);
		float height = fm.getHeight();
		float x = 0;
		float y = 0;

		switch(halign) {
		case CENTER_ALIGN:
			x = bounds.getMinX() + (bounds.getWidth() - width) / 2f;
			break;
		case LEFT_ALIGN:
			x = bounds.getMinX();
			break;
		case RIGHT_ALIGN:
			x = bounds.getMinX() + bounds.getWidth() - width;
			break;
		default:
			throw new IllegalArgumentException();
		}

		switch(getStringAnchor()) {
		case UPPER_LEFT:
			switch(valign) {
			case CENTER_ALIGN:
				y = bounds.getMinY() + (bounds.getHeight() - height) / 2f;
				break;
			case LEFT_ALIGN:
				y = bounds.getMinY();
				break;
			case RIGHT_ALIGN:
				y = bounds.getMinY() + bounds.getHeight() - height;
				break;
			default:
				throw new IllegalArgumentException();
			}
			break;
		case LEFT_BASELINE:
			switch(valign) {
			case CENTER_ALIGN:
				y = bounds.getMinY() + (bounds.getHeight() - height) / 2f + fm.getMaxAscent();
				break;
			case LEFT_ALIGN:
				y = bounds.getMinY() + fm.getMaxAscent();
				break;
			case RIGHT_ALIGN:
				y = bounds.getMinY() + bounds.getHeight() - fm.getMaxDescent();
				break;
			default:
				throw new IllegalArgumentException();
			}
			break;
		case LOWER_LEFT:
			switch(valign) {
			case CENTER_ALIGN:
				y = bounds.getMinY() + (bounds.getHeight() + height) / 2f;
				break;
			case LEFT_ALIGN:
				y = bounds.getMinY() + fm.getHeight();
				break;
			case RIGHT_ALIGN:
				y = bounds.getMaxY();
				break;
			default:
				throw new IllegalArgumentException();
			}
			break;
		default:
			throw new IllegalArgumentException();
		}
		return new Point2f(x, y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOutlineDrawn() {
		return this.isOutlinePainted;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOutlineDrawn(boolean outlined) {
		this.isOutlinePainted = outlined;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getInteriorText() {
		return this.interiorText;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInteriorText(String interiorText) {
		this.interiorText = interiorText;
	}

	/** This method paint a string into the rectangle of this figure and
	 * not outside.
	 *
	 * @param text the text to draw.
	 * @param clip is the clipping shape.
	 * @param x x coordinate where the text must be draw.
	 * @param y y coordinate where the text must be draw.
	 */
	protected void paintClippedString(String text, Shape2f clip, float x, float y ) {
		drawString( text, x, y, clip );
	}

	/** This method paint a string into the rectangle of this figure and
	 * not outside. The text is centered on the figure.
	 *
	 * @param text the text to draw.
	 * @param figureBounds are the bounds of the figure that may be used during the drawing.
	 * @param clip is the shape that should be used for clipping.
	 */
	protected void paintClippedString(String text, Rectangle2f figureBounds, Shape2f clip) {
		Point2D p = computeTextPosition(text, figureBounds, TextAlignment.CENTER_ALIGN, TextAlignment.CENTER_ALIGN);
		drawString( text, p.getX(), p.getY(), clip );
	}

}
