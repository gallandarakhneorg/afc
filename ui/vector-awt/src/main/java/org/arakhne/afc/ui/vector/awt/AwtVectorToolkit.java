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
package org.arakhne.afc.ui.vector.awt;

import java.awt.AlphaComposite;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.GrayFilter;
import javax.swing.UIManager;

import org.arakhne.afc.math.continous.object2d.Circle2f;
import org.arakhne.afc.math.continous.object2d.Ellipse2f;
import org.arakhne.afc.math.continous.object2d.Path2f;
import org.arakhne.afc.math.continous.object2d.PathIterator2f;
import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.math.continous.object2d.RoundRectangle2f;
import org.arakhne.afc.math.continous.object2d.Segment2f;
import org.arakhne.afc.math.continous.object2d.Shape2f;
import org.arakhne.afc.math.generic.PathWindingRule;
import org.arakhne.afc.math.matrix.Transform2D;
import org.arakhne.afc.ui.awt.AwtUtil;
import org.arakhne.afc.ui.awt.SupportedShape;
import org.arakhne.afc.ui.awt.VirtualizableShape;
import org.arakhne.afc.ui.vector.Color;
import org.arakhne.afc.ui.vector.Composite;
import org.arakhne.afc.ui.vector.Dimension;
import org.arakhne.afc.ui.vector.Font;
import org.arakhne.afc.ui.vector.FontMetrics;
import org.arakhne.afc.ui.vector.FontStyle;
import org.arakhne.afc.ui.vector.Image;
import org.arakhne.afc.ui.vector.Margins;
import org.arakhne.afc.ui.vector.Paint;
import org.arakhne.afc.ui.vector.Stroke;
import org.arakhne.afc.ui.vector.Stroke.EndCap;
import org.arakhne.afc.ui.vector.Stroke.LineJoin;
import org.arakhne.afc.ui.vector.VectorToolkit;
import org.arakhne.afc.vmutil.OperatingSystem;

/** AWT implementation of the generic Window toolkit.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public class AwtVectorToolkit extends VectorToolkit {

	/**
	 */
	public AwtVectorToolkit() {
		//
	}

	@Override
	protected boolean isSupported() {
		return !OperatingSystem.ANDROID.isCurrentOS();
	}

	@Override
	protected Composite createComposite(float alpha) {
		return new AwtComposite(AlphaComposite.SRC_IN, alpha);
	}

	@Override
	protected Paint createPaint(Object paintObject) {
		return new AwtPaint((java.awt.Paint)paintObject);
	}

	@Override
	protected Font createFont(String name, FontStyle style, float size) {
		return new AwtFont(name, style, size);
	}

	@Override
	protected Image createImage(URL url) {
		try {
			java.awt.Image awtImage = ImageIO.read(url);
			return new AwtImage(awtImage);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected Image createImage(InputStream stream) {
		try {
			java.awt.Image awtImage = ImageIO.read(stream);
			return new AwtImage(awtImage);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected Image createImage(int width, int height, boolean isAlpha) {
		return new AwtBufferedImage(width, height, isAlpha);
	}

	@Override
	protected Stroke createStroke(float width, LineJoin lineJoin, EndCap endCap, float mitterLimit, float[] dashes, float dashPhase) {
		return new AwtStroke(width, lineJoin, endCap, mitterLimit, dashes, dashPhase);
	}

	@Override
	protected Color createColor(int red, int green, int blue, int alpha) {
		return new AwtColor(red, green, blue, alpha);
	}

	@Override
	protected Color createColor(Object rawColorObject) {
		java.awt.Color c = (java.awt.Color)rawColorObject;
		return new AwtColor(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	}

	@Override
	protected Dimension createDimension(float width, float height) {
		return new AwtDimension(width, height);
	}

	@Override
	protected Margins createMargins(float top, float left, float right,
			float bottom) {
		return new AwtMargins(top, left, right, bottom);
	}

	@Override
	protected Composite createComposite(Object compositeObject) {
		return new AwtComposite((java.awt.Composite)compositeObject);
	}

	@Override
	protected Font createFont(Object fontObject) {
		return new AwtFont((java.awt.Font)fontObject);
	}

	@Override
	protected FontMetrics createFontMetrics(Object metricsObject) {
		return new AwtFontMetrics((java.awt.FontMetrics)metricsObject);
	}

	@Override
	protected FontMetrics createFontMetrics(Font font) {
		AwtFont awtFont = (AwtFont)font;
		java.awt.FontMetrics fm = new java.awt.FontMetrics(awtFont.getFont()) {
			private static final long serialVersionUID = 3136667595495307776L;
		};
		return new AwtFontMetrics(fm);
	}

	@Override
	protected Stroke createStroke(Object strokeObject) {
		return new AwtStroke((java.awt.BasicStroke)strokeObject);
	}

	@Override
	protected Image createImage(Object imageObject) {
		if (imageObject instanceof java.awt.Image) {
			return new AwtImage((java.awt.Image)imageObject);
		}
		else if (imageObject instanceof javax.swing.ImageIcon) {
			return new AwtImage(((javax.swing.ImageIcon)imageObject).getImage());
		}
		else if (imageObject instanceof javax.swing.Icon) {
			javax.swing.Icon ic = (javax.swing.Icon)imageObject;
			AwtBufferedImage img = new AwtBufferedImage(ic.getIconWidth(), ic.getIconHeight(), true);
			java.awt.Graphics g = img.getGraphics();
			ic.paintIcon(null, g, 0, 0);
			g.dispose();
			return img;
		}
		throw new IllegalArgumentException();
	}

	@Override
	protected Shape2f createShape(Object shapeObject) {
		if (shapeObject instanceof Shape2f)
			return (Shape2f)shapeObject;
		SupportedShape type = SupportedShape.getTypeOf(shapeObject.getClass());
		if (type!=null) {
			switch(type) {
			case RECTANGLE2D:
			{ 
				Rectangle2D r = (Rectangle2D)shapeObject;
				return new Rectangle2f((float)r.getMinX(), (float)r.getMinY(), (float)r.getWidth(), (float)r.getHeight());
			}
			case LINE:
			{ 
				Line2D l = (Line2D)shapeObject;
				return new Segment2f((float)l.getX1(), (float)l.getY1(), (float)l.getX2(), (float)l.getY2());
			}
			case ELLIPSE:
			{
				Ellipse2D q = (Ellipse2D)shapeObject;
				return new Ellipse2f(
						(float)q.getMinX(), (float)q.getMinY(),
						(float)q.getWidth(), (float)q.getHeight());
			}
			case ROUND_RECTANGLE:
			{
				RoundRectangle2D q = (RoundRectangle2D)shapeObject;
				return new RoundRectangle2f(
						(float)q.getMinX(), (float)q.getMinY(),
						(float)q.getWidth(), (float)q.getHeight(),
						(float)q.getArcWidth(), (float)q.getArcHeight());
			}
			case PATH:
			{
				Path2D p = (Path2D)shapeObject;
				return toPath(p.getPathIterator(null));
			}
			case POLYGON:
			{
				Polygon p = (Polygon)shapeObject;
				Path2f pp = toPath(p.getPathIterator(null));
				pp.closePath();
				return pp;
			}
			case AREA:
			{
				Area p = (Area)shapeObject;
				Path2f pp = toPath(p.getPathIterator(null));
				pp.closePath();
				return pp;
			}
			case QUAD_CURVE:
			{
				QuadCurve2D q = (QuadCurve2D)shapeObject;
				Path2f pp = new Path2f();
				pp.moveTo((float)q.getX1(), (float)q.getY1());
				pp.quadTo(
						(float)q.getCtrlX(), (float)q.getCtrlY(),
						(float)q.getX2(), (float)q.getY2());
				return pp;
			}
			case CUBIC_CURVE:
			{
				CubicCurve2D q = (CubicCurve2D)shapeObject;
				Path2f pp = new Path2f();
				pp.moveTo((float)q.getX1(), (float)q.getY1());
				pp.curveTo(
						(float)q.getCtrlX1(), (float)q.getCtrlY1(),
						(float)q.getCtrlX2(), (float)q.getCtrlY2(),
						(float)q.getX2(), (float)q.getY2());
				return pp;
			}
			case ARC:
			{
				Arc2D arc = (Arc2D)shapeObject;
				return toPath(arc.getPathIterator(null));
			}
			case VIRTUALIZABLE_SHAPE:
			{
				VirtualizableShape vs = (VirtualizableShape)shapeObject;
				return toPath(vs.getPathIterator(null));
			}
			default:
			}
		}
		throw new IllegalArgumentException();
	}
	
	private static Path2f toPath(PathIterator pi) {
		Path2f pp = new Path2f(getWindingRule(pi));
		float[] coords = new float[6];
		while (!pi.isDone()) {
			switch(pi.currentSegment(coords)) {
			case PathIterator.SEG_MOVETO:
				pp.moveTo(coords[0], coords[1]);
				break;
			case PathIterator.SEG_LINETO:
				pp.lineTo(coords[0], coords[1]);
				break;
			case PathIterator.SEG_QUADTO:
				pp.quadTo(coords[0], coords[1], coords[2], coords[3]);
				break;
			case PathIterator.SEG_CUBICTO:
				pp.curveTo(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
				break;
			case PathIterator.SEG_CLOSE:
				pp.closePath();
				break;
			default:
			}
			pi.next();
		}
		return pp;
	}
	
	private static PathWindingRule getWindingRule(PathIterator pi) {
		PathWindingRule rule;
		switch(pi.getWindingRule()) {
		case PathIterator.WIND_NON_ZERO:
			rule = PathWindingRule.NON_ZERO;
			break;
		case PathIterator.WIND_EVEN_ODD:
			rule = PathWindingRule.EVEN_ODD;
			break;
		default:
			rule = PathWindingRule.NON_ZERO;
			break;
		}
		return rule;
	}

	@Override
	protected <T> T toNativeUIObject(Class<T> type, Object o) {
		if (o instanceof NativeWrapper) {
			return ((NativeWrapper)o).getNativeObject(type);
		}
		if (o instanceof PathIterator2f) {
			return type.cast(new AwtPathIterator((PathIterator2f)o));
		}
		if (o instanceof Rectangle2f) {
			Rectangle2f r = (Rectangle2f)o;
			return type.cast(new Rectangle2D.Float(
					r.getMinX(), r.getMinY(),
					r.getWidth(), r.getHeight()));
		}
		if (o instanceof RoundRectangle2f) {
			RoundRectangle2f r = (RoundRectangle2f)o;
			return type.cast(new RoundRectangle2D.Float(
					r.getMinX(), r.getMinY(),
					r.getWidth(), r.getHeight(),
					r.getArcWidth(), r.getArcHeight()));
		}
		if (o instanceof Circle2f) {
			Circle2f r = (Circle2f)o;
			return type.cast(new Ellipse2D.Float(
					r.getX()-r.getRadius(), r.getY()-r.getRadius(),
					r.getX()+r.getRadius(), r.getY()+r.getRadius()));
		}
		if (o instanceof Ellipse2f) {
			Ellipse2f r = (Ellipse2f)o;
			return type.cast(new Ellipse2D.Float(
					r.getMinX(), r.getMinY(),
					r.getWidth(), r.getHeight()));
		}
		if (o instanceof Segment2f) {
			Segment2f r = (Segment2f)o;
			return type.cast(new Line2D.Float(
					r.getX1(), r.getY1(),
					r.getX2(), r.getY2()));
		}
		if (o instanceof Path2f) {
			return type.cast(new AwtPath((Path2f)o));
		}
		return type.cast(o);
	}

	@Override
	protected Font getDefaultFont() {
		return new AwtFont(java.awt.Font.decode(null));
	}

	@Override
	protected Image createTransparentImage(Image imageObject, float transparency) {
		java.awt.Image aImg = toNativeUIObject(java.awt.Image.class, imageObject);
		aImg = AwtUtil.getTransparencyFilteredImage(aImg, transparency);
		return new AwtImage(aImg);
	}

	@Override
	protected Image makeTransparentImage(Image imageObject, float transparency) {
		java.awt.Image aImg = toNativeUIObject(java.awt.Image.class, imageObject);
		aImg = AwtUtil.getTransparencyFilteredImage(aImg, transparency);
		return new AwtImage(aImg);
	}

	@Override
	protected Transform2D createTransform(Object affineTransform) {
		AffineTransform tr = (AffineTransform)affineTransform;
		return new Transform2D(
				(float)tr.getScaleX(), (float)tr.getShearX(), (float)tr.getTranslateX(),
				(float)tr.getShearY(), (float)tr.getScaleY(), (float)tr.getTranslateY());
	}

	@Override
	protected void write(Image image, String type, OutputStream stream)
			throws IOException {
		ImageIO.write(toNativeUIObject(RenderedImage.class, image), type, stream);
	}

	@Override
	protected <T> T findObjectWithId(int id, Class<T> type) {
		return null;
	}

	@Override
	protected int HSBtoRGB(float hue, float saturation, float brightness) {
		return java.awt.Color.HSBtoRGB(hue, saturation, brightness);
	}

	@Override
	protected Image createColorizedImage(Image imageObject,
			Color filtering_color, float alpha) {
		if (filtering_color==null || imageObject==null) return imageObject;
		java.awt.Image source = nativeUIObject(java.awt.Image.class, imageObject);
        if (source==null) return null;
        java.awt.Image img = ColorFilter.createFilteredImage(
        		source,
        		nativeUIObject(java.awt.Color.class, filtering_color),
        		alpha);
        return new AwtImage(img);
	}

	/** This class permits to filter the color of an icon.
	 * 
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class ColorFilter extends java.awt.image.RGBImageFilter {

		/** Indicates the filtering color.
		 */
		private final int red;

		/** Indicates the filtering color.
		 */
		private final int blue;

		/** Indicates the filtering color.
		 */
		private final int green;

		/** Alpha blending value.
		 */
		private final float alpha;

		/**
		 * Creates a color-filtered image
		 * 
		 * @param i is the image to filter
		 * @param filtering_color is the color used to filter.
		 * @param alpha indicates how the icon and the color are merged. The value
		 * is in <code>[0;1]</code>. A value of <code>0</code> means that the color 
		 * of the icon is used. A value of <code>1</code> means that the given color
		 * is used in place of the original colors. A value of <code>0.5</code>
		 * means that the resulting color is at half way between the icon's color
		 * and the given color. 
		 * @return the result of the filtering.
		 */
		public static java.awt.Image createFilteredImage(java.awt.Image i, java.awt.Color filtering_color, float alpha) {
			ColorFilter filter = new ColorFilter(filtering_color, alpha);
			java.awt.image.ImageProducer prod = new java.awt.image.FilteredImageSource(i.getSource(), filter);
			java.awt.Image filteredImage = Toolkit.getDefaultToolkit().createImage(prod);
			return filteredImage;
		}

		/**
		 * Constructs a ColorFilter object that filters a color image to a 
		 * color-scale image. It is similar to {@link GrayFilter} but not for
		 * a specifical color. 
		 *
		 * @param filtering_color is the color used to filter.
		 * @param alpha indicates how the icon and the color are merged. The value
		 * is in <code>[0;1]</code>. A value of <code>0</code> means that the color 
		 * of the icon is used. A value of <code>1</code> means that the given color
		 * is used in place of the original colors. A value of <code>0.5</code>
		 * means that the resulting color is at half way between the icon's color
		 * and the given color. 
		 */
		public ColorFilter(java.awt.Color filtering_color, float alpha) {
			this.red = filtering_color.getRed();
			this.green = filtering_color.getGreen();
			this.blue = filtering_color.getBlue();
			this.alpha = (alpha<0f) ? 0f : ((alpha>1f) ? 1f : alpha);

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
			int color_r = ((rgb >> 16) & 0xff); 
			int color_g = ((rgb >> 8) & 0xff); 
			int color_b = (rgb & 0xff);

			color_r = (int)((this.red + color_r) * this.alpha);
			if (color_r<0) color_r = 0;
			if (color_r>255) color_r = 0;

			color_g = (int)((this.green + color_g) * this.alpha);
			if (color_g<0) color_g = 0;
			if (color_g>255) color_g = 0;

			color_b = (int)((this.blue + color_b) * this.alpha);
			if (color_b<0) color_b = 0;
			if (color_b>255) color_b = 0;

			return (rgb & 0xff000000) | (color_r << 16) | (color_g << 8) | color_b;
		}

	}

	@Override
	protected Color createSelectionBackground() {
		return color(UIManager.getColor("Tree.selectionBackground")); //$NON-NLS-1$
	}

	@Override
	protected Color createSelectionForeground() {
		return color(UIManager.getColor("Tree.selectionForeground")); //$NON-NLS-1$
	}

}