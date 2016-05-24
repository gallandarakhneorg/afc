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

package org.arakhne.afc.ui.vector.awt;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.net.URL;

import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.math.continous.object2d.Shape2f;
import org.arakhne.afc.math.matrix.Transform2D;
import org.arakhne.afc.ui.Graphics2DLOD;
import org.arakhne.afc.ui.StringAnchor;
import org.arakhne.afc.ui.vector.AbstractVectorGraphics2D;
import org.arakhne.afc.ui.vector.Color;
import org.arakhne.afc.ui.vector.Font;
import org.arakhne.afc.ui.vector.FontMetrics;
import org.arakhne.afc.ui.vector.Image;
import org.arakhne.afc.ui.vector.ImageObserver;
import org.arakhne.afc.ui.vector.Paint;
import org.arakhne.afc.ui.vector.Stroke;
import org.arakhne.afc.ui.vector.VectorToolkit;

/** This graphic context permits to display
 *  some vector data with a level of details.
 *
 * @param <G> is the type of the graphics pointed by this VectorGraphics2D.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public class AwtVectorGraphics2D<G extends Graphics2D> extends AbstractVectorGraphics2D {

	/** Delegate.
	 */
	protected final G delegate;

	private final Graphics2DLOD lod;
	private final StringAnchor stringAnchor;
	
	/**
	 * @param delegate
	 * @param lod
	 * @param stringAnchor
	 */
	public AwtVectorGraphics2D(G delegate, Graphics2DLOD lod, StringAnchor stringAnchor) {
		this.delegate = delegate;
		this.lod = lod;
		this.stringAnchor = stringAnchor;
	}
	
	@Override
	public G getNativeGraphics2D() {
		return this.delegate;
	}
	
	private void setPaint() {
		Color c = getFillColor();
		if (c!=null) this.delegate.setColor(VectorToolkit.nativeUIObject(java.awt.Color.class, c));
		Paint p = getPaint();
		if (p!=null) this.delegate.setPaint(VectorToolkit.nativeUIObject(java.awt.Paint.class, p));
	}

	private void setOutlineColor() {
		Color c = getOutlineColor();
		if (c!=null) this.delegate.setColor(VectorToolkit.nativeUIObject(java.awt.Color.class, c));
	}

	@Override
	public Graphics2DLOD getLOD() {
		return this.lod;
	}

	@Override
	public StringAnchor getStringAnchor() {
		return this.stringAnchor;
	}

	@Override
	public void drawPoint(float x, float y) {
		preDrawing();
		setPaint();
		this.delegate.fill(new Rectangle2D.Float(x-.5f, y-.5f, 1f, 1f));
		postDrawing();
	}

	@Override
	public Font getFont() {
		return VectorToolkit.font(this.delegate.getFont());
	}

	@Override
	public void setFont(Font font) {
		this.delegate.setFont(VectorToolkit.nativeUIObject(java.awt.Font.class, font));
	}

	@Override
	public FontMetrics getFontMetrics() {
		return VectorToolkit.fontMetrics(this.delegate.getFontMetrics());
	}

	@Override
	public FontMetrics getFontMetrics(Font f) {
		java.awt.Font af = VectorToolkit.nativeUIObject(java.awt.Font.class, f);
		return VectorToolkit.fontMetrics(this.delegate.getFontMetrics(af));
	}

	@Override
	public Shape2f getClip() {
		return VectorToolkit.shape(this.delegate.getClip());
	}

	@Override
	public void setClip(Shape2f clip) {
		this.delegate.setClip(VectorToolkit.nativeUIObject(java.awt.Shape.class, clip));
	}

	@Override
	public void clip(Shape2f clip) {
		this.delegate.clip(VectorToolkit.nativeUIObject(java.awt.Shape.class, clip));
	}
	
	@Override
	public boolean drawImage(URL imageURL, Image img, float dx1, float dy1,
			float dx2, float dy2, int sx1, int sy1, int sx2, int sy2) {
		return drawImage(imageURL, img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
	}

	@Override
	public boolean drawImage(URL imageURL, Image img, float dx1, float dy1,
			float dx2, float dy2, int sx1, int sy1, int sx2, int sy2,
			ImageObserver observer) {
		preDrawing();
		java.awt.image.ImageObserver obs;
		if (observer==null) {
			obs = null;
		}
		else {
			obs = new ObserverWrapper(observer);
		}
		boolean drawn = this.delegate.drawImage(
				VectorToolkit.nativeUIObject(java.awt.Image.class, img),
				(int)dx1, (int)dy1, (int)dx2, (int)dy2,
				sx1, sy1, sx2, sy2,
				obs);
		if (isOutlineDrawn()) {
			setOutlineColor();
			int x = Math.min((int)dx1, (int)dx2);
			int y = Math.min((int)dy1, (int)dy2);
			int w = Math.abs((int)dx1 - (int)dx2);
			int h = Math.abs((int)dy1 - (int)dy2);
			this.delegate.drawRect(x, y, w, h);
		}
		postDrawing();
		return drawn;
	}

	@Override
	public void draw(Shape2f s) {
		preDrawing();
		Shape shp = null;
		if (isInteriorPainted()) {
			setPaint();
			shp = VectorToolkit.nativeUIObject(Shape.class, s);
			this.delegate.fill(shp);
		}
		setOutlineColor();
		String text = getInteriorText();
		if (text!=null && !text.isEmpty()) {
			paintString(
					text,
					s.toBoundingBox(),
					s);
		}
		if (isOutlineDrawn()) {
			if (shp==null) shp = VectorToolkit.nativeUIObject(Shape.class, s);
			this.delegate.draw(shp);
		}
		postDrawing();
	}

	@Override
	protected void paintString(String text, float x, float y, Shape2f clip) {
		setOutlineColor();
		if (clip==null) {
			this.delegate.drawString(text, x, y);
		}
		else {
			Shape old = this.delegate.getClip();
			this.delegate.clip(VectorToolkit.nativeUIObject(Shape.class, clip));
			this.delegate.drawString(text, x, y);
			this.delegate.setClip(old);
		}
	}

	@Override
	public void transform(Transform2D Tx) {
		AffineTransform tr = new AffineTransform(Tx.getM00(), Tx.getM10(), Tx.getM01(), Tx.getM11(), Tx.getM12(), Tx.getM12());
		this.delegate.transform(tr);
	}

	@Override
	public Transform2D setTransform(Transform2D Tx) {
		AffineTransform old = this.delegate.getTransform();
		AffineTransform tr = new AffineTransform(Tx.getM00(), Tx.getM10(), Tx.getM01(), Tx.getM11(), Tx.getM02(), Tx.getM12());
		this.delegate.setTransform(tr);
		if (old==null) return null;
		return new Transform2D(
				(float)tr.getScaleX(),
				(float)tr.getShearX(),
				(float)tr.getTranslateX(),
				(float)tr.getShearY(),
				(float)tr.getScaleY(),
				(float)tr.getTranslateY());
	}

	@Override
	public Transform2D getTransform() {
		AffineTransform tr = this.delegate.getTransform();
		if (tr==null) return null;
		return new Transform2D(
				(float)tr.getScaleX(),
				(float)tr.getShearX(),
				(float)tr.getTranslateX(),
				(float)tr.getShearY(),
				(float)tr.getScaleY(),
				(float)tr.getTranslateY());
	}

	@Override
	public void setBackground(Color color) {
		this.delegate.setBackground(new java.awt.Color(color.getRGB(), true));
	}

	@Override
	public Color getBackground() {
		java.awt.Color c = this.delegate.getBackground();
		return VectorToolkit.color(c.getRGB(), true);
	}

	@Override
	public void setComposite(org.arakhne.afc.ui.vector.Composite composite) {
		this.delegate.setComposite(VectorToolkit.nativeUIObject(java.awt.Composite.class, composite));
	}

	@Override
	public org.arakhne.afc.ui.vector.Composite getComposite() {
		return VectorToolkit.composite(this.delegate.getComposite());
	}

	@Override
	public void setStroke(Stroke stroke) {
		this.delegate.setStroke(VectorToolkit.nativeUIObject(java.awt.Stroke.class, stroke));
	}

	@Override
	public Stroke getStroke() {
		return VectorToolkit.stroke(this.delegate.getStroke());
	}

	@Override
	public void dispose() {
		this.delegate.dispose();
		super.dispose();
	}

	@Override
	public void clear(Shape2f s) {
		Rectangle2f bb = s.toBoundingBox();
		this.delegate.setColor(this.delegate.getBackground());
		this.delegate.fill(new Rectangle2D.Float(
				bb.getMinX(),
				bb.getMinY(),
				bb.getWidth(),
				bb.getHeight()));
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

	/**
	 * This is the swing-based implementation of a VectorGraphics2D and a ZoomableContext.
	 *  
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class ObserverWrapper implements java.awt.image.ImageObserver {

		private final ImageObserver observer;
		
		/**
		 * @param observer
		 */
		public ObserverWrapper(ImageObserver observer) {
			assert(observer!=null);
			this.observer = observer;
		}

		@Override
		public boolean imageUpdate(
				java.awt.Image img, int infoflags, int x,
				int y, int width, int height) {
			assert(this.observer!=null);
			return this.observer.imageUpdate(
					VectorToolkit.image(img), x, y, width, height);
		}
		
	}

}