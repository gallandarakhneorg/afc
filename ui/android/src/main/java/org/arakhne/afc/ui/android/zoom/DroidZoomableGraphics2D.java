/* 
 * $Id$
 * 
 * Copyright (C) 2013 Stephane GALLAND.
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
package org.arakhne.afc.ui.android.zoom;

import java.net.URL;

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
import org.arakhne.afc.ui.vector.Stroke;
import org.arakhne.afc.ui.vector.VectorToolkit;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region.Op;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * This is the droid-based implementation of a VectorGraphics2D and a ZoomableContext.
 *  
 * @author $Author: galland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DroidZoomableGraphics2D extends AbstractVectorGraphics2D implements ZoomableContext {
	
	/** Convert the droid matrix to Arakhne transformation matrix.
	 * 
	 * @param m is the matrix to convert to a transformation.
	 * @param scale is the scaling factor to apply.
	 * @return the Arakhne transformation matrix.
	 */
	public static Transform2D convertMatrix(Matrix m, float scale) {
		assert(m!=null);
		if (m.isIdentity()) return new Transform2D();
		float[] values = new float[9];
		m.getValues(values);
		return new Transform2D(
				ZoomableContextUtil.pixel2logical_size(values[0], scale),
				values[1],
				ZoomableContextUtil.pixel2logical_size(values[2], scale),
				values[3],
				ZoomableContextUtil.pixel2logical_size(values[4], scale),
				ZoomableContextUtil.pixel2logical_size(values[5], scale));
	}

	/** Convert the Arakhne transformation matrix to droid matrix.
	 * 
	 * @param t is the transformation to convert to a matrix.
	 * @param scale is the scaling factor to apply.
	 * @return droid matrix.
	 */
	public static Matrix convertTransformation(Transform2D t, float scale) {
		assert(t!=null);
		Matrix at = new Matrix();
		if (!t.isIdentity()) {
			float[] values = new float[] {
					ZoomableContextUtil.logical2pixel_size(t.m00, scale),
					t.m01,
					ZoomableContextUtil.logical2pixel_size(t.m02, scale),
					t.m10,
					ZoomableContextUtil.logical2pixel_size(t.m11, scale),
					ZoomableContextUtil.logical2pixel_size(t.m12, scale),
					t.m20, t.m21, t.m22
			};
			at.setValues(values);
		}
		return at;
	}

	/** Set the color of the given paint to the given color.
	 * This function tries the Android color objects that
	 * are able to represent a color (ARGB color, Drawable).
	 * 
	 * @param paint
	 * @param color
	 */
	protected static void setColor(Paint paint, Color color) {
		Object androidColor = VectorToolkit.nativeUIObject(Object.class, color);
		if (androidColor instanceof Drawable) {
			//paint.setColor((Drawable)androidColor);
		}
		else if (androidColor instanceof Number) {
			paint.setColor(((Number)androidColor).intValue());
		}
	}


	/** Android canvas.
	 */
	protected final Canvas canvas;

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

	/** Android painter to use for filling the shapes.
	 */
	protected Paint fillPainter;

	/** Android painter to use for drawing the outlines of the shapes.
	 */
	protected Paint linePainter;

	/** Android painter to use for drawing the texts.
	 */
	protected Paint fontPainter;

	/** Current background color.
	 * 
	 * @see #defaultBackground
	 */
	protected Color background;

	private final int initialSaveCount;
	
	/** Scaling factor to apply when drawing.
	 */
	protected final float scale;
	
	/** Transformation to apply to center the objects on the view.
	 */
	protected final CenteringTransform centeringTransform;
	
	/** Temp rectangle used in internal computations.
	 */
	protected final Rectangle2f tmpRect = new Rectangle2f();

	/** Default color to fill the shapes.
	 * 
	 * @see #fillPainter
	 */
	protected final Color defaultFillColor;

	/** Default color to outline the shapes.
	 * 
	 * @see #linePainter
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

	/**
	 * @param canvas
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
	public DroidZoomableGraphics2D(
			Canvas canvas,
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
		this.initialSaveCount = canvas.getSaveCount();
		this.defaultLevelOfDetail = this.levelOfDetail = isAntiAlias ? Graphics2DLOD.NORMAL_LEVEL_OF_DETAIL : Graphics2DLOD.LOW_LEVEL_OF_DETAIL;
		resetPainters();
	}

	@Override
	public Canvas getNativeGraphics2D() {
		return this.canvas;
	}

	@Override
	public void dispose() {
		this.background = null;
		this.fillPainter = null;
		this.fontPainter = null;
		this.linePainter = null;
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
		while (this.canvas.getSaveCount() > this.initialSaveCount) {
			this.canvas.restore();
		}
	}

	/** Reset the painters to the default attribute values.
	 */
	protected void resetPainters() {
		boolean isNormalLod = this.levelOfDetail.compareTo(Graphics2DLOD.NORMAL_LEVEL_OF_DETAIL)>=0;

		this.fillPainter = new Paint();
		this.fillPainter.setStyle(Style.FILL);
		this.fillPainter.setAntiAlias(isNormalLod);
		this.linePainter = new Paint();
		this.linePainter.setStyle(Style.STROKE);
		this.linePainter.setAntiAlias(isNormalLod);
		this.fontPainter = new Paint();
		this.fontPainter.setStyle(Style.FILL_AND_STROKE);
		this.fontPainter.setAntiAlias(isNormalLod);
		this.fontPainter.setTextSize(
				ZoomableContextUtil.logical2pixel_size(
						this.fontPainter.getTextSize(),
						this.scale));
	}

	/** {@inheritDoc}
	 */
	@Override
	protected void log(String message) {
		Log.d(toString(), message);
	}

	/** {@inheritDoc}
	 */
	@Override
	protected void log(String message, Throwable exception) {
		Log.d(toString(), message, exception);
	}

	@Override
	public Color setOutlineColor(Color color) {
		setColor(this.linePainter, color);
		setColor(this.fontPainter, color);
		return super.setOutlineColor(color);
	}

	@Override
	public Color setFillColor(Color color) {
		setColor(this.fillPainter, color);
		return super.setFillColor(color);
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
			this.fillPainter.setAntiAlias(isNormalLod);
			this.linePainter.setAntiAlias(isNormalLod);
			this.fontPainter.setAntiAlias(isNormalLod);
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
		this.canvas.drawRect(px-.5f, py-.5f, px+.5f, py+.5f, this.fillPainter);
		postDrawing();
	}

	@Override
	public Font getFont() {
		Paint scaledFont = new Paint(this.fontPainter);
		scaledFont.setTextSize(
				ZoomableContextUtil.pixel2logical_size(this.fontPainter.getTextSize(), this.scale));
		return VectorToolkit.font(scaledFont);
	}

	@Override
	public void setFont(Font font) {
		Paint p = VectorToolkit.nativeUIObject(Paint.class, font);
		assert(p!=null);
		this.fontPainter = new Paint(p);
		this.fontPainter.setTextSize(
				ZoomableContextUtil.logical2pixel_size(p.getTextSize(), this.scale));
	}

	@Override
	public FontMetrics getFontMetrics() {
		Paint scaledFont = new Paint(this.fontPainter);
		scaledFont.setTextSize(
				ZoomableContextUtil.pixel2logical_size(this.fontPainter.getTextSize(), this.scale));
		return VectorToolkit.fontMetrics(scaledFont);
	}

	@Override
	public FontMetrics getFontMetrics(Font f) {
		return VectorToolkit.fontMetrics(f);
	}

	@Override
	public Shape2f getClip() {
		Rect r = this.canvas.getClipBounds();
		Rectangle2f rr = new Rectangle2f(r.left, r.right, r.width(), r.height());
		ZoomableContextUtil.pixel2logical(rr, this.centeringTransform, this.scale);
		return rr;
	}

	@Override
	public void setClip(Shape2f clip) {
		Rectangle2f r = clip.toBoundingBox();
		ZoomableContextUtil.logical2pixel(r, this.centeringTransform, this.scale);
		this.canvas.clipRect(r.getMinX(), r.getMinY(), r.getMaxX(), r.getMaxY(), Op.REPLACE);
	}

	@Override
	public void clip(Shape2f clip) {
		Rectangle2f r = clip.toBoundingBox();
		ZoomableContextUtil.logical2pixel(r, this.centeringTransform, this.scale);
		this.canvas.clipRect(r.getMinX(), r.getMinY(), r.getMaxX(), r.getMaxY(), Op.UNION);
	}

	@Override
	public boolean drawImage(URL imageURL, Image img, float dx1, float dy1,
			float dx2, float dy2, int sx1, int sy1, int sx2, int sy2) {
		preDrawing();
		Bitmap bitmap = VectorToolkit.nativeUIObject(Bitmap.class, img);
		assert(bitmap!=null);
		this.tmpRect.setFromCorners(dx1, dy1, dx2, dy2);
		ZoomableContextUtil.logical2pixel(this.tmpRect, this.centeringTransform, this.scale);
		Rect srcRect = new Rect(sx1, sy1, sx2, sy2);
		RectF dstRect = new RectF(this.tmpRect.getMinX(), this.tmpRect.getMinY(), this.tmpRect.getMaxX(), this.tmpRect.getMaxY());
		this.canvas.drawBitmap(bitmap, srcRect, dstRect, VectorToolkit.getObjectWithId(0, Paint.class));
		postDrawing();
		return true;
	}

	@Override
	public boolean drawImage(URL imageURL, Image img, float dx1, float dy1,
			float dx2, float dy2, int sx1, int sy1, int sx2, int sy2,
			ImageObserver observer) {
		preDrawing();
		Bitmap bitmap = VectorToolkit.nativeUIObject(Bitmap.class, img);
		assert(bitmap!=null);
		this.tmpRect.setFromCorners(dx1, dy1, dx2, dy2);
		ZoomableContextUtil.logical2pixel(this.tmpRect, this.centeringTransform, this.scale);
		Rect srcRect = new Rect(sx1, sy1, sx2, sy2);
		RectF dstRect = new RectF(this.tmpRect.getMinX(), this.tmpRect.getMinY(), this.tmpRect.getMaxX(), this.tmpRect.getMaxY());
		this.canvas.drawBitmap(bitmap, srcRect, dstRect, VectorToolkit.getObjectWithId(0, Paint.class));
		postDrawing();
		return true;
	}

	@Override
	public void clear(Shape2f s) {
		Paint old = this.fillPainter;
		this.fillPainter = VectorToolkit.getObjectWithId(0, Paint.class);
		this.fillPainter.setAlpha(1);
		Color b = getBackground();
		if (b!=null) {
			setColor(this.fillPainter, b);
		}
		drawOnCanvas(s, this.fillPainter);
		this.fillPainter = old;
		postDrawing();
	}

	private void drawOnCanvas(Shape2f s, Paint painter) {
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
					this.canvas.drawRect(this.tmpRect.getMinX(), this.tmpRect.getMinY(), this.tmpRect.getMaxX(), this.tmpRect.getMaxY(), painter);
					return;
				}
				if (s instanceof Ellipse2f) {
					Ellipse2f r = (Ellipse2f)s;
					this.tmpRect.set(r);
					ZoomableContextUtil.logical2pixel(this.tmpRect, this.centeringTransform, this.scale);
					this.canvas.drawOval(
							new RectF(this.tmpRect.getMinX(), this.tmpRect.getMinY(), this.tmpRect.getMaxX(), this.tmpRect.getMaxY()), painter);
					return;
				}
				if (s instanceof Circle2f) {
					Circle2f r = (Circle2f)s;
					float cx = ZoomableContextUtil.logical2pixel_x(r.getX(), this.centeringTransform, this.scale);
					float cy = ZoomableContextUtil.logical2pixel_y(r.getY(), this.centeringTransform, this.scale);
					float radius = ZoomableContextUtil.logical2pixel_size(r.getRadius(), this.scale);
					this.canvas.drawCircle(cx, cy, radius, painter);
					return;
				}
				if (s instanceof RoundRectangle2f) {
					RoundRectangle2f r = (RoundRectangle2f)s;
					this.tmpRect.set(r);
					ZoomableContextUtil.logical2pixel(this.tmpRect, this.centeringTransform, this.scale);
					float aw = ZoomableContextUtil.logical2pixel_size(r.getArcWidth()/2f, this.scale);
					float ah = ZoomableContextUtil.logical2pixel_size(r.getArcHeight()/2f, this.scale);
					this.canvas.drawRoundRect(
							new RectF(this.tmpRect.getMinX(), this.tmpRect.getMinY(), this.tmpRect.getMaxX(), this.tmpRect.getMaxY()),
							aw, ah, painter);
					return;
				}
				if (s instanceof Segment2f) {
					Segment2f l = (Segment2f)s;
					this.canvas.drawLine(
							ZoomableContextUtil.logical2pixel_x(l.getX1(), this.centeringTransform, this.scale),
							ZoomableContextUtil.logical2pixel_y(l.getY1(), this.centeringTransform, this.scale),
							ZoomableContextUtil.logical2pixel_x(l.getX2(), this.centeringTransform, this.scale),
							ZoomableContextUtil.logical2pixel_y(l.getY2(), this.centeringTransform, this.scale),
							painter);
					return;
				}

				path = s.getPathIterator();
			}
			if (path!=null) {
				path = ZoomableContextUtil.logical2pixel(path, this.centeringTransform, this.scale);
				Path droidPath = new Path();
				if (path.getWindingRule()==PathWindingRule.EVEN_ODD) {
					droidPath.setFillType(FillType.EVEN_ODD);
				}
				PathElement2f e;
				while (path.hasNext()) {
					e = path.next();
					switch(e.type) {
					case MOVE_TO:
						droidPath.moveTo(e.toX, e.toY);
						break;
					case LINE_TO:
						droidPath.lineTo(e.toX, e.toY);
						break;
					case CURVE_TO:
						droidPath.cubicTo(e.ctrlX1, e.ctrlY1, e.ctrlX2, e.ctrlY2, e.toX, e.toY);
						break;
					case QUAD_TO:
						droidPath.quadTo(e.ctrlX1, e.ctrlY1, e.toX, e.toY);
						break;
					case CLOSE:
						droidPath.close();
						break;
					default:
					}
				}
				this.canvas.drawPath(droidPath, painter);
			}
			else {
				throw new IllegalArgumentException(s.toString());
			}
		}
	}

	@Override
	public void draw(Shape2f s) {
		preDrawing();
		if (isInteriorPainted()) {
			drawOnCanvas(s, this.fillPainter);
		}
		String text = getInteriorText();
		if (text!=null && !text.isEmpty()) {
			paintClippedString(
					text,
					s.toBoundingBox(),
					s);
		}
		if (isOutlineDrawn()) {
			drawOnCanvas(s, this.linePainter);
		}
		postDrawing();
	}
	
	@Override
	public void drawString(String str, float x, float y) {
		preDrawing();
		this.canvas.drawText(str,
				ZoomableContextUtil.logical2pixel_x(x, this.centeringTransform, this.scale),
				ZoomableContextUtil.logical2pixel_y(y, this.centeringTransform, this.scale),
				this.fontPainter);
		postDrawing();
	}

	@Override
	public void drawString(String str, float x, float y, Shape2f clip) {
		preDrawing();
		Shape2f c = getClip();
		clip(clip);
		this.canvas.drawText(str,
				ZoomableContextUtil.logical2pixel_x(x, this.centeringTransform, this.scale),
				ZoomableContextUtil.logical2pixel_y(y, this.centeringTransform, this.scale),
				this.fontPainter);
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
		this.canvas.skew(
				ZoomableContextUtil.logical2pixel_size(shx, this.scale),
				ZoomableContextUtil.logical2pixel_size(shy, this.scale));
	}

	@Override
	public void transform(Transform2D Tx) {
		this.canvas.concat(convertTransformation(Tx, this.scale));
	}

	@Override
	public Transform2D setTransform(Transform2D Tx) {
		Transform2D old = convertMatrix(this.canvas.getMatrix(), this.scale);
		this.canvas.setMatrix(convertTransformation(Tx, this.scale));
		return old;
	}

	@Override
	public Transform2D getTransform() {
		return convertMatrix(this.canvas.getMatrix(), this.scale);
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
		int alpha = (int)(composite.getAlpha() * 255);
		this.fillPainter.setAlpha(alpha);
		this.linePainter.setAlpha(alpha);
		this.fontPainter.setAlpha(alpha);
	}

	@Override
	public Composite getComposite() {
		return VectorToolkit.composite(1);
	}

	@Override
	public void setStroke(Stroke stroke) {
		this.linePainter.setStrokeMiter(stroke.getMiterLimit());
		this.fontPainter.setStrokeMiter(stroke.getMiterLimit());

		Cap cap;
		switch(stroke.getEndCap()) {
		case BUTT:
			cap = Cap.BUTT;
			break;
		case SQUARE:
			cap = Cap.SQUARE;
			break;
		case ROUND:
			cap = Cap.ROUND;
			break;
		default:
			throw new IllegalStateException();
		}
		this.linePainter.setStrokeCap(cap);
		this.fontPainter.setStrokeCap(cap);

		Join join;
		switch(stroke.getLineJoin()) {
		case BEVEL:
			join = Join.BEVEL;
			break;
		case MITER:
			join = Join.MITER;
			break;
		case ROUND:
			join = Join.ROUND;
			break;
		default:
			throw new IllegalStateException();
		}
		this.linePainter.setStrokeJoin(join);
		this.fontPainter.setStrokeJoin(join);

		this.linePainter.setStrokeWidth(stroke.getLineWidth());
		this.fontPainter.setStrokeWidth(stroke.getLineWidth());

		float[] dashes = stroke.getDashArray();
		PathEffect pe = null;
		if (dashes!=null && dashes.length>=2) {
			pe = new DashPathEffect(dashes, stroke.getDashPhase());
		}
		this.linePainter.setPathEffect(pe);
		this.fontPainter.setPathEffect(pe);
	}

	@Override
	public Stroke getStroke() {
		return VectorToolkit.stroke(this.linePainter);
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

}
