/* 
 * $Id$
 * 
 * Copyright (C) 2002  St&eacute;phane GALLAND, Mahdi Hannoun
 * Copyright (C) 2004-2013  St&eacute;phane GALLAND
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
package org.arakhne.afc.ui.swing.zoom;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import org.arakhne.afc.math.matrix.Transform2D;
import org.arakhne.afc.ui.CenteringTransform;
import org.arakhne.afc.ui.Graphics2DLOD;
import org.arakhne.afc.ui.StringAnchor;
import org.arakhne.afc.ui.ZoomableContext;
import org.arakhne.afc.ui.ZoomableContextUtil;
import org.arakhne.afc.ui.vector.AbstractVectorGraphics2D;
import org.arakhne.afc.ui.vector.Color;
import org.arakhne.afc.ui.vector.Composite;
import org.arakhne.afc.ui.vector.Font;
import org.arakhne.afc.ui.vector.FontMetrics;
import org.arakhne.afc.ui.vector.Image;
import org.arakhne.afc.ui.vector.ImageObserver;
import org.arakhne.afc.ui.vector.Paint;
import org.arakhne.afc.ui.vector.Stroke;
import org.arakhne.afc.ui.vector.VectorToolkit;

/**
 * This is the swing-based implementation of a VectorGraphics2D and a ZoomableContext.
 *  
 * @author $Author: galland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ZoomableGraphics2D extends AbstractVectorGraphics2D
implements ZoomableContext {

	/** Convert the Awt affine transformation to Arakhne transformation matrix.
	 * 
	 * @param m is the matrix to convert to a transformation.
	 * @param scale is the scaling factor to apply.
	 * @return the Arakhne transformation matrix.
	 */
	public static Transform2D convertAffineTransform(AffineTransform m, float scale) {
		assert(m!=null);
		if (m.isIdentity()) return new Transform2D();
		return new Transform2D(
				ZoomableContextUtil.pixel2logical_size((float)m.getScaleX(), scale),
				(float)m.getShearX(),
				ZoomableContextUtil.pixel2logical_size((float)m.getTranslateX(), scale),
				(float)m.getShearY(),
				ZoomableContextUtil.pixel2logical_size((float)m.getScaleY(), scale),
				ZoomableContextUtil.pixel2logical_size((float)m.getTranslateY(), scale));
	}
	
	/** Convert the Arakhne transformation matrix to droid matrix.
	 * 
	 * @param t is the transformation to convert to a matrix.
	 * @param scale is the scaling factor to apply.
	 * @return droid matrix.
	 */
	public static AffineTransform convertTransformation(Transform2D t, float scale) {
		assert(t!=null);
		AffineTransform at;
		if (!t.isIdentity()) {
			at = new AffineTransform(
					ZoomableContextUtil.logical2pixel_size(t.m00, scale),
					t.m01,
					ZoomableContextUtil.logical2pixel_size(t.m02, scale),
					t.m10,
					ZoomableContextUtil.logical2pixel_size(t.m11, scale),
					ZoomableContextUtil.logical2pixel_size(t.m12, scale));
		}
		else {
			at = new AffineTransform();
		}
		return at;
	}

	/** Swing canvas.
	 */
	protected final Graphics2D canvas;

	/** Default color to draw the background.
	 * 
	 * @see #background
	 */
	protected final Color defaultBackground;
	
	/** Default Level-of-Detail.
	 * 
	 * @see #levelOfDetail
	 */
	protected final Graphics2DLOD defaultLevelOfDetail;

	/** Current Level-of-Detail.
	 * 
	 * @see #defaultLevelOfDetail
	 */
	protected Graphics2DLOD levelOfDetail;

	/** Current background color.
	 * 
	 * @see #defaultBackground
	 */
	protected Color background;

	/** Scaling factor to apply when drawing.
	 */
	protected final float scale;
	
	/** Transformation to apply to center the objects on the view.
	 */
	protected final CenteringTransform centeringTransform;
	
	/** Temp rectangle used in internal computations.
	 */
	protected final Rectangle2f tmpRect = new Rectangle2f();

	/** Temp rectangle used in internal computations.
	 */
	protected final Rectangle2D tmpRect2 = new Rectangle2D.Float();

	/** Default color to fill the shapes.
	 */
	protected final Color defaultFillColor;

	/** Default color to outline the shapes.
	 */
	protected final Color defaultLineColor;

	/** Scaling sensitivity.
	 */
	protected final float scalingSensitivity;

	/** Coordinate of the point that is drawn at the center of the view.
	 */
	protected final float focusX;

	/** Coordinate of the point that is drawn at the center of the view.
	 */
	protected final float focusY;

	/** Maximal value for the scale factor.
	 */
	protected final float maxScale;

	/** Minimal value for the scale factor.
	 */
	protected final float minScale;
	
	/** Default Awt font.
	 */
	private final java.awt.Font defaultAwtFont;

	/**
	 * @param canvas is the Swing context into which to draw.
	 * @param defaultFillColor is the default color for filling.
	 * @param defaultLineColor is the default color for the lines.
	 * @param scaleFactor is the scaling factor to apply.
	 * @param centeringTransform is the transformation used to draw the objects at the center of the view.
	 * @param background is the background color.
	 * @param isAntiAlias indicates if antialiasing is used when drawing.
	 * @param scalingSensitivity is the sensitivity of the scaling actions.
	 * @param focusX is the coordinate of the focus point.
	 * @param focusY is the coordinate of the focus point.
	 * @param minScaleFactor is the minimal allowed scaling factor.
	 * @param maxScaleFactor is the maximal allowed scaling factor.
	 */
	public ZoomableGraphics2D(
			Graphics2D canvas,
			Color defaultFillColor,
			Color defaultLineColor,
			float scaleFactor,
			CenteringTransform centeringTransform,
			Color background,
			boolean isAntiAlias,
			float scalingSensitivity,
			float focusX,
			float focusY,
			float minScaleFactor,
			float maxScaleFactor) {
		super(
				defaultFillColor,
				defaultLineColor,
				null,
				true,
				true,
				null);
		this.defaultAwtFont = canvas.getFont();
		this.defaultFillColor = defaultFillColor;
		this.defaultLineColor = defaultLineColor;
		this.scalingSensitivity = scalingSensitivity;
		this.focusX = focusX;
		this.focusY = focusY;
		this.minScale = minScaleFactor;
		this.maxScale = maxScaleFactor;
		this.canvas = canvas;
		this.scale = scaleFactor;
		this.centeringTransform = centeringTransform;
		this.defaultBackground = this.background = background;
		this.defaultLevelOfDetail = this.levelOfDetail = isAntiAlias ? Graphics2DLOD.NORMAL_LEVEL_OF_DETAIL : Graphics2DLOD.LOW_LEVEL_OF_DETAIL;
		resetPainters();
	}

	@Override
	public Graphics2D getNativeGraphics2D() {
		return this.canvas;
	}

	@Override
	public void dispose() {
		this.background = null;
		this.levelOfDetail = null;
		super.dispose();
	}

	@Override
	public void reset() {
		super.reset();
		this.levelOfDetail = this.defaultLevelOfDetail;
		this.background = this.defaultBackground;
		setFillColor(this.defaultFillColor);
		setOutlineColor(this.defaultLineColor);
		resetPainters();
	}

	/** Reset the painters to the default attribute values.
	 */
	protected void resetPainters() {
		boolean isNormalLod = this.levelOfDetail.compareTo(Graphics2DLOD.NORMAL_LEVEL_OF_DETAIL)>=0;
		this.canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
				isNormalLod
				? RenderingHints.VALUE_ANTIALIAS_ON
				: RenderingHints.VALUE_ANTIALIAS_OFF);
		this.canvas.setComposite(AlphaComposite.SrcOver);
		this.canvas.setStroke(new BasicStroke());
		float rawSize = logical2pixel_size(this.defaultAwtFont.getSize2D());
		this.canvas.setFont(this.defaultAwtFont.deriveFont(rawSize));
	}
	
	/** {@inheritDoc}
	 */
	@Override
	protected void log(String message) {
		Logger.getLogger(getClass().getCanonicalName()).log(Level.FINEST, message);
	}

	/** {@inheritDoc}
	 */
	@Override
	protected void log(String message, Throwable exception) {
		Logger.getLogger(getClass().getCanonicalName()).log(Level.FINEST, message, exception);
	}

	@Override
	public Graphics2DLOD getLOD() {
		return this.levelOfDetail;
	}

	/** Change the LOD of the graphics2D.
	 * 
	 * @param lod
	 */
	public void setLOD(Graphics2DLOD lod) {
		if (lod!=null) {
			this.levelOfDetail = lod;
			boolean isNormalLod = this.levelOfDetail.compareTo(Graphics2DLOD.NORMAL_LEVEL_OF_DETAIL)>=0;
			this.canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
					isNormalLod
					? RenderingHints.VALUE_ANTIALIAS_ON
					: RenderingHints.VALUE_ANTIALIAS_OFF);
		}
	}

	@Override
	public StringAnchor getStringAnchor() {
		return StringAnchor.LEFT_BASELINE;
	}

	@Override
	public void drawPoint(float x, float y) {
		preDrawing();
		float px = ZoomableContextUtil.logical2pixel_x(x, this.centeringTransform, this.scale);
		float py = ZoomableContextUtil.logical2pixel_y(y, this.centeringTransform, this.scale);
		this.tmpRect2.setFrame(px-.5f, py-.5f, 1f, 1f);
		appliesOutlineAttributes();
		this.canvas.fill(this.tmpRect2);
		postDrawing();
	}

	@Override
	public Font getFont() {
		java.awt.Font f = this.canvas.getFont();
		f = f.deriveFont(
				ZoomableContextUtil.pixel2logical_size(
						f.getSize(),
						this.scale));
		return VectorToolkit.font(f);
	}

	@Override
	public void setFont(Font font) {
		java.awt.Font f = VectorToolkit.nativeUIObject(java.awt.Font.class, font);
		assert(f!=null);
		f = f.deriveFont(
				ZoomableContextUtil.logical2pixel_size(f.getSize(), this.scale));
		this.canvas.setFont(f);
	}

	@Override
	public FontMetrics getFontMetrics() {
		java.awt.Font f = this.canvas.getFont();
		f = f.deriveFont(
				ZoomableContextUtil.pixel2logical_size(f.getSize(), this.scale));
		return VectorToolkit.fontMetrics(this.canvas.getFontMetrics(f));
	}

	@Override
	public FontMetrics getFontMetrics(Font f) {
		return VectorToolkit.fontMetrics(f);
	}

	@Override
	public Shape2f getClip() {
		Rectangle2f rr = null;
		Shape r = this.canvas.getClip();
		if (r!=null) {
			Rectangle2D b = r.getBounds2D();
			rr = new Rectangle2f(
					(float)b.getMinX(), (float)b.getMinY(),
					(float)b.getWidth(), (float)b.getHeight());
			ZoomableContextUtil.pixel2logical(rr, this.centeringTransform, this.scale);
		}
		return rr;
	}

	@Override
	public void setClip(Shape2f clip) {
		if (clip!=null) {
			Rectangle2f r = clip.toBoundingBox();
			ZoomableContextUtil.logical2pixel(r, this.centeringTransform, this.scale);
			this.tmpRect2.setFrame(
					r.getMinX(), r.getMinY(),
					r.getWidth(), r.getHeight());
			this.canvas.setClip(this.tmpRect2);
		}
		else {
			this.canvas.setClip(null);
		}
	}

	@Override
	public void clip(Shape2f clip) {
		Rectangle2f r = clip.toBoundingBox();
		ZoomableContextUtil.logical2pixel(r, this.centeringTransform, this.scale);
		this.tmpRect2.setFrame(
				r.getMinX(), r.getMinY(),
				r.getWidth(), r.getHeight());
		this.canvas.clip(this.tmpRect2);
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
		java.awt.Image bitmap = VectorToolkit.nativeUIObject(java.awt.Image.class, img);
		assert(bitmap!=null);
		this.tmpRect.setFromCorners(dx1, dy1, dx2, dy2);
		ZoomableContextUtil.logical2pixel(this.tmpRect, this.centeringTransform, this.scale);
		ObserverWrapper obs;
		if (observer==null) {
			obs = null;
		}
		else {
			obs = new ObserverWrapper(observer);
		}
		boolean b = this.canvas.drawImage(
				bitmap,
				(int)this.tmpRect.getMinX(), (int)this.tmpRect.getMinY(),
				(int)this.tmpRect.getMaxX(), (int)this.tmpRect.getMaxY(),
				sx1, sy1, sx2, sy2,
				VectorToolkit.nativeUIObject(java.awt.Color.class, getBackground()),
				obs);
		if (isOutlineDrawn()) {
			appliesOutlineAttributes();
			int x = Math.min((int)this.tmpRect.getMinX(), (int)this.tmpRect.getMaxX());
			int y = Math.min((int)this.tmpRect.getMinY(), (int)this.tmpRect.getMaxY());
			int w = Math.abs((int)this.tmpRect.getMinX() - (int)this.tmpRect.getMaxX());
			int h = Math.abs((int)this.tmpRect.getMinY() - (int)this.tmpRect.getMaxY());
			this.canvas.drawRect(x, y, w, h);
		}
		postDrawing();
		return b;
	}

	private Shape toAwt(Shape2f s) {
		if (s!=null) {
			PathIterator2f path = null;
			if (s instanceof Path2f) {
				path = ((Path2f)s).getPathIterator();
			}
			else {
				if (s instanceof Rectangle2f) {
					Rectangle2f r = (Rectangle2f)s;
					this.tmpRect.set(r);
					ZoomableContextUtil.logical2pixel(this.tmpRect, this.centeringTransform, this.scale);
					this.tmpRect2.setFrame(
							this.tmpRect.getMinX(), this.tmpRect.getMinY(),
							this.tmpRect.getWidth(), this.tmpRect.getHeight());
					return this.tmpRect2;
				}
				if (s instanceof Ellipse2f) {
					Ellipse2f r = (Ellipse2f)s;
					this.tmpRect.set(r);
					ZoomableContextUtil.logical2pixel(this.tmpRect, this.centeringTransform, this.scale);
					Ellipse2D se = new Ellipse2D.Float(
							this.tmpRect.getMinX(), this.tmpRect.getMinY(),
							this.tmpRect.getWidth(), this.tmpRect.getHeight());
					return se;
				}
				if (s instanceof Circle2f) {
					Circle2f r = (Circle2f)s;
					float cx = ZoomableContextUtil.logical2pixel_x(r.getX(), this.centeringTransform, this.scale);
					float cy = ZoomableContextUtil.logical2pixel_y(r.getY(), this.centeringTransform, this.scale);
					float radius = ZoomableContextUtil.logical2pixel_size(r.getRadius(), this.scale);
					float diameter = radius * 2f;
					Ellipse2D se = new Ellipse2D.Float(
							cx-radius, cy-radius,
							diameter, diameter);
					return se;
				}
				if (s instanceof RoundRectangle2f) {
					RoundRectangle2f r = (RoundRectangle2f)s;
					this.tmpRect.set(r);
					ZoomableContextUtil.logical2pixel(this.tmpRect, this.centeringTransform, this.scale);
					float aw = ZoomableContextUtil.logical2pixel_size(r.getArcWidth()/2f, this.scale);
					float ah = ZoomableContextUtil.logical2pixel_size(r.getArcHeight()/2f, this.scale);
					RoundRectangle2D rr = new RoundRectangle2D.Float(
							this.tmpRect.getMinX(), this.tmpRect.getMinY(),
							this.tmpRect.getWidth(), this.tmpRect.getHeight(),
							aw, ah);
					return rr;
				}
				if (s instanceof Segment2f) {
					Segment2f l = (Segment2f)s;
					Line2D sl = new Line2D.Float(
							ZoomableContextUtil.logical2pixel_x(l.getX1(), this.centeringTransform, this.scale),
							ZoomableContextUtil.logical2pixel_y(l.getY1(), this.centeringTransform, this.scale),
							ZoomableContextUtil.logical2pixel_x(l.getX2(), this.centeringTransform, this.scale),
							ZoomableContextUtil.logical2pixel_y(l.getY2(), this.centeringTransform, this.scale));
					return sl;
				}

				path = s.getPathIterator();
			}
			if (path!=null) {
				path = ZoomableContextUtil.logical2pixel(path, this.centeringTransform, this.scale);
				Path2D sp = new Path2D.Float();
				if (path.getWindingRule()==PathWindingRule.EVEN_ODD) {
					sp.setWindingRule(Path2D.WIND_EVEN_ODD);
				}
				PathElement2f e;
				while (path.hasNext()) {
					e = path.next();
					switch(e.type) {
					case MOVE_TO:
						sp.moveTo(e.toX, e.toY);
						break;
					case LINE_TO:
						sp.lineTo(e.toX, e.toY);
						break;
					case CURVE_TO:
						sp.curveTo(e.ctrlX1, e.ctrlY1, e.ctrlX2, e.ctrlY2, e.toX, e.toY);
						break;
					case QUAD_TO:
						sp.quadTo(e.ctrlX1, e.ctrlY1, e.toX, e.toY);
						break;
					case CLOSE:
						sp.closePath();
						break;
					default:
					}
				}
				return sp;
			}
			throw new IllegalArgumentException(s.toString());
		}
		throw new IllegalArgumentException();
	}

	private java.awt.Color toAwt(Color c) {
		if (c==null) return this.canvas.getBackground();
		return new java.awt.Color(c.getRGB(),true);
	}
	
	private void appliesFillAttributes() {
		Paint painter = getPaint();
		java.awt.Paint awtPaint = VectorToolkit.nativeUIObject(java.awt.Paint.class, painter);
		if (awtPaint!=null) {
			this.canvas.setPaint(awtPaint);
		}
		else {
			this.canvas.setColor(toAwt(getFillColor()));
		}
	}
	
	private void appliesOutlineAttributes() {
		this.canvas.setColor(toAwt(getOutlineColor()));
	}

	private void appliesTextAttributes() {
		this.canvas.setColor(toAwt(getOutlineColor()));
	}

	@Override
	public void clear(Shape2f s) {
		this.canvas.setColor(toAwt(getBackground()));
		this.canvas.fill(toAwt(s));
		postDrawing();
	}
	
	@Override
	public void draw(Shape2f s) {
		preDrawing();
		Shape awt = toAwt(s);
		if (isInteriorPainted()) {
			appliesFillAttributes();
			this.canvas.fill(awt);
		}
		String text = getInteriorText();
		if (text!=null && !text.isEmpty()) {
			paintClippedString(
					text,
					s.toBoundingBox(),
					s);
		}
		if (isOutlineDrawn()) {
			appliesOutlineAttributes();
			this.canvas.draw(awt);
		}
		postDrawing();
	}
	
	@Override
	public void drawString(String str, float x, float y) {
		preDrawing();
		appliesTextAttributes();
		FontRenderContext frc = this.canvas.getFontRenderContext();
		GlyphVector glyphs = this.canvas.getFont().createGlyphVector(frc, str);
		Shape s = glyphs.getOutline(
				ZoomableContextUtil.logical2pixel_x(x, this.centeringTransform, this.scale),
				ZoomableContextUtil.logical2pixel_y(y, this.centeringTransform, this.scale));
		assert(s!=null);
		this.canvas.fill(s);
		/*this.canvas.drawString(
				str,
				ZoomableContextUtil.logical2pixel_x(x, this.centeringTransform, this.scale),
				ZoomableContextUtil.logical2pixel_y(y, this.centeringTransform, this.scale));*∕*/
		postDrawing();
	}

	@Override
	public void drawString(String str, float x, float y, Shape2f clip) {
		preDrawing();
		Shape2f c = getClip();
		clip(clip);
		appliesTextAttributes();
		FontRenderContext frc = this.canvas.getFontRenderContext();
		GlyphVector glyphs = this.canvas.getFont().createGlyphVector(frc, str);
		Shape s = glyphs.getOutline(
				ZoomableContextUtil.logical2pixel_x(x, this.centeringTransform, this.scale),
				ZoomableContextUtil.logical2pixel_y(y, this.centeringTransform, this.scale));
		assert(s!=null);
		this.canvas.fill(s);
		/*this.canvas.drawString(
				str,
				ZoomableContextUtil.logical2pixel_x(x, this.centeringTransform, this.scale),
				ZoomableContextUtil.logical2pixel_y(y, this.centeringTransform, this.scale));*/
		setClip(c);
		postDrawing();
	}

	@Override
	public void translate(float tx, float ty) {
		this.canvas.translate(
				ZoomableContextUtil.logical2pixel_size(tx, this.scale),
				ZoomableContextUtil.logical2pixel_size(ty, this.scale));
	}

	@Override
	public void scale(float sx, float sy) {
		this.canvas.scale(
				ZoomableContextUtil.logical2pixel_size(sx, this.scale),
				ZoomableContextUtil.logical2pixel_size(sy, this.scale));
	}

	@Override
	public void rotate(float theta) {
		this.canvas.rotate((float)Math.toDegrees(theta));
	}

	@Override
	public void shear(float shx, float shy) {
		this.canvas.shear(
				ZoomableContextUtil.logical2pixel_size(shx, this.scale),
				ZoomableContextUtil.logical2pixel_size(shy, this.scale));
	}

	@Override
	public void transform(Transform2D Tx) {
		this.canvas.transform(convertTransformation(Tx, this.scale));
	}

	@Override
	public Transform2D setTransform(Transform2D Tx) {
		Transform2D old = convertAffineTransform(this.canvas.getTransform(), this.scale);
		this.canvas.setTransform(convertTransformation(Tx, this.scale));
		return old;
	}

	@Override
	public Transform2D getTransform() {
		return convertAffineTransform(this.canvas.getTransform(), this.scale);
	}

	@Override
	public void setBackground(Color color) {
		this.background = color;
	}

	@Override
	public Color getBackground() {
		return this.background;
	}

	@Override
	public void setComposite(Composite composite) {
		java.awt.Composite ac = VectorToolkit.nativeUIObject(java.awt.Composite.class, composite);
		this.canvas.setComposite(ac);
	}

	@Override
	public Composite getComposite() {
		return VectorToolkit.composite(this.canvas.getComposite());
	}

	@Override
	public void setStroke(Stroke stroke) {
		java.awt.Stroke as = VectorToolkit.nativeUIObject(java.awt.Stroke.class, stroke);
		this.canvas.setStroke(as);
	}

	@Override
	public Stroke getStroke() {
		return VectorToolkit.stroke(this.canvas.getStroke());
	}

	@Override
	public float logical2pixel_size(float l) {
		return ZoomableContextUtil.logical2pixel_size(l, this.scale);
	}

	@Override
	public float logical2pixel_x(float l) {
		return ZoomableContextUtil.logical2pixel_x(l, this.centeringTransform, this.scale);
	}

	@Override
	public float logical2pixel_y(float l) {
		return ZoomableContextUtil.logical2pixel_y(l, this.centeringTransform, this.scale);
	}

	@Override
	public float pixel2logical_size(float l) {
		return ZoomableContextUtil.pixel2logical_size(l, this.scale);
	}

	@Override
	public float pixel2logical_x(float l) {
		return ZoomableContextUtil.pixel2logical_x(l, this.centeringTransform, this.scale);
	}

	@Override
	public float pixel2logical_y(float l) {
		return ZoomableContextUtil.pixel2logical_y(l, this.centeringTransform, this.scale);
	}

	@Override
	public PathIterator2f logical2pixel(PathIterator2f p) {
		return ZoomableContextUtil.logical2pixel(p, this.centeringTransform, this.scale);
	}

	@Override
	public PathIterator2f pixel2logical(PathIterator2f p) {
		return ZoomableContextUtil.pixel2logical(p, this.centeringTransform, this.scale);
	}

	@Override
	public void logical2pixel(Segment2f s) {
		ZoomableContextUtil.logical2pixel(s, this.centeringTransform, this.scale);
	}

	@Override
	public void pixel2logical(Segment2f s) {
		ZoomableContextUtil.pixel2logical(s, this.centeringTransform, this.scale);
	}

	@Override
	public void logical2pixel(RoundRectangle2f r) {
		ZoomableContextUtil.logical2pixel(r, this.centeringTransform, this.scale);
	}

	@Override
	public void pixel2logical(RoundRectangle2f r) {
		ZoomableContextUtil.pixel2logical(r, this.centeringTransform, this.scale);
	}

	@Override
	public void logical2pixel(Point2f p) {
		ZoomableContextUtil.logical2pixel(p, this.centeringTransform, this.scale);
	}

	@Override
	public void pixel2logical(Point2f p) {
		ZoomableContextUtil.pixel2logical(p, this.centeringTransform, this.scale);
	}

	@Override
	public void logical2pixel(Ellipse2f e) {
		ZoomableContextUtil.logical2pixel(e, this.centeringTransform, this.scale);
	}

	@Override
	public void pixel2logical(Ellipse2f e) {
		ZoomableContextUtil.pixel2logical(e, this.centeringTransform, this.scale);
	}

	@Override
	public void logical2pixel(Circle2f r) {
		ZoomableContextUtil.logical2pixel(r, this.centeringTransform, this.scale);
	}

	@Override
	public void pixel2logical(Circle2f r) {
		ZoomableContextUtil.pixel2logical(r, this.centeringTransform, this.scale);
	}

	@Override
	public void logical2pixel(Rectangle2f r) {
		ZoomableContextUtil.logical2pixel(r, this.centeringTransform, this.scale);
	}

	@Override
	public void pixel2logical(Rectangle2f r) {
		ZoomableContextUtil.pixel2logical(r, this.centeringTransform, this.scale);
	}

	@Override
	public Shape2f logical2pixel(Shape2f s) {
		return ZoomableContextUtil.logical2pixel(s, this.centeringTransform, this.scale);
	}

	@Override
	public Shape2f pixel2logical(Shape2f s) {
		return ZoomableContextUtil.pixel2logical(s, this.centeringTransform, this.scale);
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
	public float getScalingFactor() {
		return this.scale;
	}

	@Override
	public float getMaxScalingFactor() {
		return this.maxScale;
	}

	@Override
	public float getMinScalingFactor() {
		return this.minScale;
	}

	@Override
	public boolean isXAxisInverted() {
		return this.centeringTransform.isXAxisFlipped();
	}

	@Override
	public boolean isYAxisInverted() {
		return this.centeringTransform.isYAxisFlipped();
	}

	/**
	 * This is the swing-based implementation of a VectorGraphics2D and a ZoomableContext.
	 *  
	 * @author $Author: galland$
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
