/* 
 * $Id$
 * 
 * Copyright (C) 2002 Stephane GALLAND, Madhi HANNOUN, Marc BAUMGARTNER.
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
package org.arakhne.afc.ui.vector.android;

import java.net.URL;

import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.math.continous.object2d.Shape2f;
import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.math.matrix.Transform2D;
import org.arakhne.afc.ui.Graphics2DLOD;
import org.arakhne.afc.ui.StringAnchor;
import org.arakhne.afc.ui.TextAlignment;
import org.arakhne.afc.ui.vector.AbstractVectorGraphics2D;
import org.arakhne.afc.ui.vector.Color;
import org.arakhne.afc.ui.vector.Composite;
import org.arakhne.afc.ui.vector.Font;
import org.arakhne.afc.ui.vector.FontMetrics;
import org.arakhne.afc.ui.vector.Image;
import org.arakhne.afc.ui.vector.ImageObserver;
import org.arakhne.afc.ui.vector.Paint;
import org.arakhne.afc.ui.vector.Stroke;
import org.arakhne.afc.ui.vector.VectorGraphics2D;

/** Implementation of a graphics context which is
 * delegating to another graphics context.
 *
 * @param <G> is the type of the delegate.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DelegatedVectorGraphics2D<G extends VectorGraphics2D> extends AbstractVectorGraphics2D {

	/** Delegate.
	 */
	protected final G delegate;
	
	/**
	 * @param context
	 */
	public DelegatedVectorGraphics2D(G context) {
		super(
				context.getFillColor(),
				context.getOutlineColor(),
				context.getPaint(),
				context.isInteriorPainted(),
				context.isOutlineDrawn(),
				context.getInteriorText());
		this.delegate = context;
		
	}
	
	@Override
	public Object getNativeGraphics2D() {
		return this.delegate.getNativeGraphics2D();
	}
	
	@Override
	public Graphics2DLOD getLOD() {
		return this.delegate.getLOD();
	}

	@Override
	public StringAnchor getStringAnchor() {
		return this.delegate.getStringAnchor();
	}

	@Override
	public Font getFont() {
		return this.delegate.getFont();
	}

	@Override
	public void setFont(Font font) {
		this.delegate.setFont(font);
	}

	@Override
	public FontMetrics getFontMetrics() {
		return this.delegate.getFontMetrics();
	}

	@Override
	public FontMetrics getFontMetrics(Font f) {
		return this.delegate.getFontMetrics(f);
	}

	@Override
	public Shape2f getClip() {
		return this.delegate.getClip();
	}

	@Override
	public void setClip(Shape2f clip) {
		this.delegate.setClip(clip);
	}

	@Override
	public void clip(Shape2f clip) {
		this.delegate.clip(clip);
	}
	
	@Override
	public void transform(Transform2D Tx) {
		this.delegate.transform(Tx);
	}

	@Override
	public void translate(float tx, float ty) {
		this.delegate.translate(tx, ty);
	}

	@Override
	public void scale(float sx, float sy) {
		this.delegate.scale(sx, sy);
	}

	@Override
	public void rotate(float theta) {
		this.delegate.rotate(theta);
	}

	@Override
	public void shear(float shx, float shy) {
		this.delegate.shear(shx, shy);
	}

	@Override
	public Transform2D setTransform(Transform2D Tx) {
		return this.delegate.setTransform(Tx);
	}

	@Override
	public Transform2D getTransform() {
		return this.delegate.getTransform();
	}

	@Override
	public void setBackground(Color color) {
		this.delegate.setBackground(color);
	}

	@Override
	public Color getBackground() {
		return this.delegate.getBackground();
	}

	@Override
	public void clear(Shape2f s) {
		this.delegate.clear(s);
	}

	@Override
	public void setComposite(Composite composite) {
		this.delegate.setComposite(composite);
	}

	@Override
	public Composite getComposite() {
		return this.delegate.getComposite();
	}

	@Override
	public void setStroke(Stroke stroke) {
		this.delegate.setStroke(stroke);
	}

	@Override
	public Stroke getStroke() {
		return this.delegate.getStroke();
	}
				
	@Override
	public Point2D computeTextPosition(String text, Rectangle2f bounds,
			TextAlignment halign, TextAlignment valign) {
		return this.delegate.computeTextPosition(text, bounds, halign, valign);
	}

	@Override
	public Font getDefaultFont() {
		return this.delegate.getDefaultFont();
	}
			
	@Override
	public void dispose() {
		this.delegate.dispose();
		super.dispose();
	}
	
	@Override
	public void reset() {
		this.delegate.reset();
		super.reset();
	}	
	
	@Override
	public boolean drawImage(URL imageURL, Image img, float dx1, float dy1,
			float dx2, float dy2, int sx1, int sy1, int sx2, int sy2) {
		preDrawing();
		Image i = onImagePainting(getFillColor(), getOutlineColor(), getPaint(),
				isInteriorPainted(), isOutlineDrawn(), getInteriorText(), img);
		boolean b = this.delegate.drawImage(imageURL, i, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2);
		postDrawing();
		return b;
	}

	@Override
	public boolean drawImage(URL imageURL, Image img, float dx1, float dy1,
			float dx2, float dy2, int sx1, int sy1, int sx2, int sy2,
			ImageObserver observer) {
		preDrawing();
		Image i = onImagePainting(getFillColor(), getOutlineColor(), getPaint(),
				isInteriorPainted(), isOutlineDrawn(), getInteriorText(), img);
		boolean b = this.delegate.drawImage(imageURL, i, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
		postDrawing();
		return b;
	}

	@Override
	public void draw(Shape2f s) {
		preDrawing();
		onAttributePainting(getFillColor(), getOutlineColor(), getPaint(),
				isInteriorPainted(), isOutlineDrawn(), getInteriorText());
		this.delegate.draw(s);
		postDrawing();
	}

	@Override
	public void drawString(String str, float x, float y) {
		preDrawing();
		onAttributePainting(getFillColor(), getOutlineColor(), getPaint(),
				isInteriorPainted(), isOutlineDrawn(), getInteriorText());
		this.delegate.drawString(str, x, y);
		postDrawing();
	}

	@Override
	public void drawString(String str, float x, float y, Shape2f clip) {
		preDrawing();
		onAttributePainting(getFillColor(), getOutlineColor(), getPaint(),
				isInteriorPainted(), isOutlineDrawn(), getInteriorText());
		this.delegate.drawString(str, x, y, clip);
		postDrawing();
	}

	@Override
	public void drawPoint(float x, float y) {
		preDrawing();
		onAttributePainting(getFillColor(), getOutlineColor(), getPaint(),
				isInteriorPainted(), isOutlineDrawn(), getInteriorText());
		this.delegate.drawPoint(x,y);
		postDrawing();
	}
	
	/** Invoked to set the painting attributes just before drawing a shape.
	 * 
	 * @param fillColor is the color used to fill the shapes.
	 * @param outlineColor is the color used to draw the outline of the shapes.
	 * @param paint is the painter.
	 * @param drawInterior indicates if the interior of the shapes are painted.
	 * @param drawOutline indicates if the outline of the shapes are painted.
	 * @param interiorText is the text to drawn inside the shapes.
	 */
	protected void onAttributePainting(Color fillColor, Color outlineColor, Paint paint, boolean drawInterior, boolean drawOutline, String interiorText) {
		if (fillColor!=null) this.delegate.setFillColor(fillColor);
		if (outlineColor!=null) this.delegate.setOutlineColor(outlineColor);
		if (paint!=null) this.delegate.setPaint(paint);
		this.delegate.setInteriorPainted(drawInterior);
		this.delegate.setOutlineDrawn(drawOutline);
		this.delegate.setInteriorText(interiorText);
	}

	/** Invoked to set the painting attributes just before drawing an image.
	 * This function invokes {@link #onAttributePainting(Color, Color, Paint, boolean, boolean, String)}
	 * to initialize the painting attributes.
	 * 
	 * @param fillColor is the color used to fill the shapes.
	 * @param outlineColor is the color used to draw the outline of the shapes.
	 * @param paint is the painter.
	 * @param drawInterior indicates if the interior of the shapes are painted.
	 * @param drawOutline indicates if the outline of the shapes are painted.
	 * @param interiorText is the text to drawn inside the shapes.
	 * @param image is the image to draw.
	 * @return the image to draw.
	 */
	protected Image onImagePainting(Color fillColor, Color outlineColor, Paint paint, boolean drawInterior, boolean drawOutline, String interiorText, Image image) {
		onAttributePainting(fillColor, outlineColor, paint, drawInterior, drawOutline, interiorText);
		return image;
	}

}
