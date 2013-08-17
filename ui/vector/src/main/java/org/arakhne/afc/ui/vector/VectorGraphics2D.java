/* 
 * $Id$
 * 
 * Copyright (C) 2005-09 Stephane GALLAND.
 * Copyright (C) 2012 Stephane GALLAND.
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

import java.net.URL;

import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.math.continous.object2d.Shape2f;
import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.math.matrix.Transform2D;
import org.arakhne.afc.ui.Graphics2DLOD;
import org.arakhne.afc.ui.StringAnchor;
import org.arakhne.afc.ui.TextAlignment;

/** This graphic context permits to display
 *  something with a vectorial approach.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface VectorGraphics2D {
	
	/** Replies the native graphical context.
	 * 
	 * @return the native graphical context.
	 */
	public Object getNativeGraphics2D();
	
	/** Release all the allocated resources.
	 */
	public void dispose();
	
	/** Reset the graphical context to its default state if possible.
	 */
	public void reset();

	/** Replies the LOD.
	 * 
	 * @return the LOD.
	 */
	public Graphics2DLOD getLOD();

	/** Replies if the objects are filled.
	 * 
	 * @return <code>true</code> if the objects are filled;
	 * otherwise <code>false</code>.
	 */
	public boolean isInteriorPainted();

	/** Set if the objects are filled.
	 * 
	 * @param painted is <code>true</code> if the objects are filled;
	 * otherwise <code>false</code>.
	 */
	public void setInteriorPainted(boolean painted);

	/** Replies if the objects are outlined.
	 * 
	 * @return <code>true</code> if the objects are outlined;
	 * otherwise <code>false</code>.
	 */
	public boolean isOutlineDrawn();

	/** Set if the objects are outlined.
	 * 
	 * @param outlined is <code>true</code> if the objects are outlined;
	 * otherwise <code>false</code>.
	 */
	public void setOutlineDrawn(boolean outlined);

	/** Replies if the text to put inside the next dranw shape.
	 * 
	 * @return the text to put in the interior
	 * of the next shape to be drawn.
	 */
	public String getInteriorText();

	/** Set if the text to put inside the next dranw shape.
	 * 
	 * @param interiorText is the text to put in the interior
	 * of the next shape to be drawn.
	 */
	public void setInteriorText(String interiorText);

	/** Replies the anchor for the strings to draw.
	 * 
	 * @return the anchor for the strings.
	 */
	public StringAnchor getStringAnchor();
	
	/** Draws a pixel.
	 *  <p>
	 *  The given coordinates will be transformed.
	 *
	 * @param x is the location where to paint the pixel.
	 * @param y is the location where to paint the pixel.
	 */
	public void drawPoint(float x, float y);

	/**
	 * Gets the current font.
	 * @return    this graphics context's current font.
	 */
	public Font getFont();

	/**
	 * Gets the default font.
	 * @return    this graphics default font.
	 */
	public Font getDefaultFont();

	/**
	 * Sets the current font.
	 * @param font is this graphics context's current font.
	 */
	public void setFont(Font font);

	/**
	 * Gets the font metrics of the current font.
	 * @return    the font metrics of this graphics 
	 *                    context's current font.
	 */
	public FontMetrics getFontMetrics();

	/**
	 * Gets the font metrics for the specified font.
	 * @return    the font metrics for the specified font.
	 * @param     f the specified font
	 */
	public FontMetrics getFontMetrics(Font f);

	/**
	 * Gets the current clipping area.
	 * This method returns the user clip, which is independent of the
	 * clipping associated with device bounds and window visibility.
	 * If no clip has previously been set, or if the clip has been 
	 * cleared using <code>setClip(null)</code>, this method returns 
	 * <code>null</code>.
	 * @return      a <code>Shape</code> object representing the 
	 *              current clipping area, or <code>null</code> if
	 *              no clip is set.
	 */
	public Shape2f getClip();

	/**
	 * Sets the current clipping area to an arbitrary clip shape.
	 * @param clip
	 */
	public void setClip(Shape2f clip);

	/**
	 * Sets the current clipping area are the union
	 * of the current clipping and an arbitrary clip shape.
	 * @param clip
	 */
	public void clip(Shape2f clip);

	/**
	 * Draws a default image inside the given bounds.
	 * The image depends on the background implementation. 
	 * 
	 * @param       dx1 the <i>x</i> coordinate of the first corner of the
	 *                    destination rectangle.
	 * @param       dy1 the <i>y</i> coordinate of the first corner of the
	 *                    destination rectangle.
	 * @param       dx2 the <i>x</i> coordinate of the second corner of the
	 *                    destination rectangle.
	 * @param       dy2 the <i>y</i> coordinate of the second corner of the
	 *                    destination rectangle.
	 */
	public void drawDefaultImage(float dx1, float dy1, float dx2, float dy2);

	/**
	 * Draws as much of the specified area of the specified image as is
	 * currently available, scaling it on the fly to fit inside the
	 * specified area of the destination drawable surface. Transparent pixels 
	 * do not affect whatever pixels are already there.
	 * 
	 * @param       imageURL is the URL of the image that is drawn.
	 * @param       img the specified image to be drawn. This method does
	 *                  nothing if <code>img</code> is null.
	 * @param       dx1 the <i>x</i> coordinate of the first corner of the
	 *                    destination rectangle.
	 * @param       dy1 the <i>y</i> coordinate of the first corner of the
	 *                    destination rectangle.
	 * @param       dx2 the <i>x</i> coordinate of the second corner of the
	 *                    destination rectangle.
	 * @param       dy2 the <i>y</i> coordinate of the second corner of the
	 *                    destination rectangle.
	 * @param       sx1 the <i>x</i> coordinate of the first corner of the
	 *                    source rectangle.
	 * @param       sy1 the <i>y</i> coordinate of the first corner of the
	 *                    source rectangle.
	 * @param       sx2 the <i>x</i> coordinate of the second corner of the
	 *                    source rectangle.
	 * @param       sy2 the <i>y</i> coordinate of the second corner of the
	 *                    source rectangle.
	 * @return   <code>false</code> if the image pixels are still changing;
	 *           <code>true</code> otherwise.
	 */
	public boolean drawImage(URL imageURL, Image img, float dx1, float dy1, float dx2, float dy2, int sx1, int sy1, int sx2, int sy2);

	/**
	 * Draws as much of the specified area of the specified image as is
	 * currently available, scaling it on the fly to fit inside the
	 * specified area of the destination drawable surface. Transparent pixels 
	 * do not affect whatever pixels are already there.
	 * 
	 * @param       imageURL is the URL of the image that is drawn.
	 * @param       img the specified image to be drawn. This method does
	 *                  nothing if <code>img</code> is null.
	 * @param       dx1 the <i>x</i> coordinate of the first corner of the
	 *                    destination rectangle.
	 * @param       dy1 the <i>y</i> coordinate of the first corner of the
	 *                    destination rectangle.
	 * @param       dx2 the <i>x</i> coordinate of the second corner of the
	 *                    destination rectangle.
	 * @param       dy2 the <i>y</i> coordinate of the second corner of the
	 *                    destination rectangle.
	 * @param       sx1 the <i>x</i> coordinate of the first corner of the
	 *                    source rectangle.
	 * @param       sy1 the <i>y</i> coordinate of the first corner of the
	 *                    source rectangle.
	 * @param       sx2 the <i>x</i> coordinate of the second corner of the
	 *                    source rectangle.
	 * @param       sy2 the <i>y</i> coordinate of the second corner of the
	 *                    source rectangle.
	 * @param       observer is the observer of the image.
	 * @return   <code>false</code> if the image pixels are still changing;
	 *           <code>true</code> otherwise.
	 */
	public boolean drawImage(URL imageURL, Image img, float dx1, float dy1, float dx2, float dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer);

	/**
	 * Strokes the outline of a <code>Shape</code> using the settings of the 
	 * current <code>VectorGraphics2D</code> context.  The rendering attributes
	 * applied include the <code>Clip</code>, <code>Transform</code>,
	 * <code>Paint</code>, <code>Composite</code> and 
	 * <code>Stroke</code> attributes.
	 * @param s the <code>Shape</code> to be rendered
	 */
	public void draw(Shape2f s);

	/**
	 * Renders the text specified by the specified <code>String</code>, 
	 * using the current text attribute state in the <code>VectorGraphics2D</code> context.
	 * The specified position depends on the text anchor replied by
	 * {@lni #getStringAnchor()}.
	 *  
	 * @param str the <code>String</code> to be rendered
	 * @param x the x coordinate of the location where the
	 * <code>String</code> should be rendered
	 * @param y the y coordinate of the location where the
	 * <code>String</code> should be rendered
	 * @throws NullPointerException if <code>str</code> is
	 *         <code>null</code>
	 */
	public void drawString(String str, float x, float y);

	/**
	 * Renders the text specified by the specified <code>String</code>, 
	 * using the current text attribute state in the <code>VectorGraphics2D</code> context.
	 * The specified position depends on the text anchor replied by
	 * {@lni #getStringAnchor()}.
	 *  
	 * @param str the <code>String</code> to be rendered
	 * @param x the x coordinate of the location where the
	 * <code>String</code> should be rendered
	 * @param y the y coordinate of the location where the
	 * <code>String</code> should be rendered
	 * @param clip is the shape outside which the text should not be rendered.
	 * @throws NullPointerException if <code>str</code> is
	 *         <code>null</code>
	 */
	public void drawString(String str, float x, float y, Shape2f clip);

	/**
	 * Composes an <code>Transform2D</code> object with the 
	 * <code>Transform</code> in this <code>VectorGraphics2D</code> according 
	 * to the rule last-specified-first-applied.  If the current
	 * <code>Transform</code> is Cx, the result of composition
	 * with Tx is a new <code>Transform</code> Cx'.  Cx' becomes the
	 * current <code>Transform</code> for this <code>VectorGraphics2D</code>.
	 * Transforming a point p by the updated <code>Transform</code> Cx' is
	 * equivalent to first transforming p by Tx and then transforming
	 * the result by the original <code>Transform</code> Cx.  In other
	 * words, Cx'(p) = Cx(Tx(p)).  A copy of the Tx is made, if necessary,
	 * so further modifications to Tx do not affect rendering.
	 * @param Tx the <code>Transform2D</code> object to be composed with 
	 * the current <code>Transform</code>
	 */
	public void transform(Transform2D Tx);

	/** CConcatenates the current <code>VectorGraphics2D</code>
     * <code>Transform2D</code> 
	 * with a translation transformation.
     * <p>
     * This is equivalent to calling <code>transform(R)</code>, where R is an
     * <code>AffineTransform</code> represented by the following matrix:
     * <pre>
     *          [   0    0    tx  ]
     *          [   0    0    ty  ]
     *          [   0    0    1   ]
     * </pre>
     * 
     * @param tx is the translation along x.
     * @param ty is the translation along x.
     */
	public void translate(float tx, float ty);
	
	/** Concatenates the current <code>VectorGraphics2D</code>
     * <code>Transform2D</code> 
	 * with a scaling transformation.
     * <p>
     * This is equivalent to calling <code>transform(R)</code>, where R is an
     * <code>AffineTransform</code> represented by the following matrix:
     * <pre>
     *          [   sx   0    0   ]
     *          [   0    sy   0   ]
     *          [   0    0    1   ]
     * </pre>
     * 
     * @param sx is the scaling along x.
     * @param sy is the scaling along x.
     */
	public void scale(float sx, float sy);
	
	/**
     * Concatenates the current <code>VectorGraphics2D</code>
     * <code>Transform2D</code> with a rotation transform.
     * Subsequent rendering is rotated by the specified radians relative
     * to the previous origin.
     * <p>
     * This is equivalent to calling <code>transform(R)</code>, where R is an
     * <code>AffineTransform</code> represented by the following matrix:
     * <pre>
     *          [   cos(theta)    -sin(theta)    0   ]
     *          [   sin(theta)     cos(theta)    0   ]
     *          [       0              0         1   ]
     * </pre>
     * 
     * @param theta the angle of rotation in radians
     */
    public abstract void rotate(float theta);

    /**
     * Concatenates the current <code>VectorGraphics2D</code>
     * <code>Transform2D</code> with a shearing transform.
     * <p>
     * This is equivalent to calling <code>transform(SH)</code>, where SH
     * is an <code>AffineTransform</code> represented by the following
     * matrix:
     * <pre>
     *          [   1   shx   0   ]
     *          [  shy   1    0   ]
     *          [   0    0    1   ]
     * </pre>
     * 
     * @param shx the multiplier by which coordinates are shifted in
     * the positive X axis direction as a function of their Y coordinate
     * @param shy the multiplier by which coordinates are shifted in
     * the positive Y axis direction as a function of their X coordinate
     */
    public abstract void shear(float shx, float shy);
    
	/**
	 * Overwrites the Transform in the <code>VectorGraphics2D</code> context.
	 * WARNING: This method should <b>never</b> be used to apply a new
	 * coordinate transform on top of an existing transform because the 
	 * <code>VectorGraphics2D</code> might already have a transform that is
	 * needed for other purposes, such as rendering Swing 
	 * components or applying a scaling transformation to adjust for the
	 * resolution of a printer.  
	 * <p>To add a coordinate transform, use the 
	 * <code>transform</code>, <code>rotate</code>, <code>scale</code>,
	 * or <code>shear</code> methods.  The <code>setTransform</code> 
	 * method is intended only for restoring the original 
	 * <code>VectorGraphics2D</code> transform after rendering, as shown in this
	 * example:
	 * <pre><blockquote>
	 * // Get the current transform
	 * Transform2D saveAT = g2.getTransform();
	 * // Perform transformation
	 * g2d.transform(...);
	 * // Render
	 * g2d.draw(...);
	 * // Restore original transform
	 * g2d.setTransform(saveAT);
	 * </blockquote></pre>
	 *
	 * @param Tx the <code>Transform2D</code> that was retrieved
	 *           from the <code>getTransform</code> method
	 * @return the previous transformation.
	 */
	public Transform2D setTransform(Transform2D Tx);

	/**
	 * Returns a copy of the current <code>Transform</code> in the 
	 * <code>VectorGraphics2D</code> context.
	 * @return the current <code>Transform2D</code> in the 
	 *             <code>VectorGraphics2D</code> context.
	 */
	public Transform2D getTransform();

	/**
	 * Sets the background color for the <code>VectorGraphics2D</code> context. 
	 * The background color is used for clearing a region.
	 * Setting the background color 
	 * in the <code>VectorGraphics2D</code> context only affects the subsequent      
	 * <code>clear</code> calls and not the background color of the  
	 * graphical component.
	 * @param color the background color that issued in
	 * subsequent calls to <code>clear</code>
	 */
	public void setBackground(Color color);

	/**
	 * Returns the background color used for clearing a region.
	 * @return the current <code>VectorGraphics2D</code> <code>Color</code>,
	 * which defines the background color.
	 */
	public Color getBackground();

	/**
	 * Clear the area covered by the given shape.
	 * The background color is used to fill the area.
	 * <p>
	 * This function is equivalent to:
	 * <pre><code>
	 * c = this.getFillColor();
	 * this.setFillColor(this.getBackgroundColor());
	 * this.fill(s);
	 * this.setFillColor(c);
	 * </code></pre>
	 * 
	 * @param s is the area to clear.
	 */
	public void clear(Shape2f s);

	/**
	 * Sets the object that permits to paint the interior of the objects.
	 * 
	 * @param paint the paint
	 * @return the old paint.
	 */
	public Paint setPaint(Paint paint);
	
	/**
	 * Sets the outline color.
	 * 
	 * @param color is the foreground color.
	 * @return the old color.
	 */
	public Color setOutlineColor(Color color);
	
	/**
	 * Sets the fill color.
	 * 
	 * @param color is the filling color.
	 * @return the old color
	 */
	public Color setFillColor(Color color);

	/** Replies the outline color.
	 * 
	 * @return the outline color.
	 */
	public Color getOutlineColor();
	
	/** Replies the fill color.
	 * 
	 * @return the fill color.
	 */
	public Color getFillColor();

	/**
	 * Returns the object that permits to paint the interior of the objects.
	 * 
	 * @return the paint.
	 */
	public Paint getPaint();

	/**
	 * Sets the composite.
	 * 
	 * @param composite the composite
	 */
	public void setComposite(Composite composite);
	
	/**
	 * Returns the composite.
	 * 
	 * @return the composite.
	 */
	public Composite getComposite();

	/**
	 * Sets the stroke.
	 * 
	 * @param stroke the stroke.
	 */
	public void setStroke(Stroke stroke);
	
	/**
	 * Returns the stroke.
	 * 
	 * @return the stroke.
	 */
	public Stroke getStroke();

	/** Compute the position of a text with the specified alignements
	 * in the specified bounds.
	 *
	 * @param text is the text for which the position should be computed.
	 * @param bounds are the bounds to consider
	 * @param halign is the horizontal alignment.
	 * @param valign is the vertical alignment.
	 * @return the position of the text.
	 */
	public Point2D computeTextPosition(String text, Rectangle2f bounds, TextAlignment halign, TextAlignment valign);
	
}