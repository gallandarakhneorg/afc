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
package org.arakhne.afc.ui.vector.android;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.arakhne.afc.math.continous.object2d.Shape2f;
import org.arakhne.afc.math.matrix.Transform2D;
import org.arakhne.afc.ui.vector.Color;
import org.arakhne.afc.ui.vector.Composite;
import org.arakhne.afc.ui.vector.Dimension;
import org.arakhne.afc.ui.vector.Font;
import org.arakhne.afc.ui.vector.FontMetrics;
import org.arakhne.afc.ui.vector.FontStyle;
import org.arakhne.afc.ui.vector.Image;
import org.arakhne.afc.ui.vector.Margins;
import org.arakhne.afc.ui.vector.Pdf;
import org.arakhne.afc.ui.vector.Stroke;
import org.arakhne.afc.ui.vector.Stroke.EndCap;
import org.arakhne.afc.ui.vector.Stroke.LineJoin;
import org.arakhne.afc.ui.vector.VectorGraphics2D;
import org.arakhne.afc.ui.vector.VectorToolkit;
import org.arakhne.vmutil.OperatingSystem;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

/** Android implementation of the generic Window toolkit.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AndroidVectorToolkit extends VectorToolkit {

	/**
	 */
	public AndroidVectorToolkit() {
		//
	}

	@Override
	protected boolean isSupported() {
		return OperatingSystem.ANDROID.isCurrentOS();
	}

	@Override
	protected void postDrawing(VectorGraphics2D context) {
		// do not call super function to avoid to reset the current context.
	}

	@Override
	protected <T> T toNativeUIObject(Class<T> type, Object o) {
		if (o instanceof NativeWrapper) {
			return ((NativeWrapper)o).getNativeObject(type);
		}
		return type.cast(o);
	}

	@Override
	protected Shape2f createShape(Object nativeObject) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Transform2D createTransform(Object affineTransform) {
		if (affineTransform==null) return null;
		Matrix matrix = (Matrix)affineTransform;
		float[] values = new float[9];
		matrix.getValues(values);
		return new Transform2D(
				values[0], values[1], values[2],
				values[3], values[4], values[5]);
	}

	@Override
	protected Dimension createDimension(float width, float height) {
		return new AndroidDimension(width, height);
	}

	@Override
	protected Margins createMargins(float top, float left, float right,
			float bottom) {
		return new AndroidMargins(top, left, right, bottom);
	}

	@Override
	protected Composite createComposite(Object compositeObject) {
		if (compositeObject==null) return null;
		return new AndroidComposite((Paint)compositeObject);
	}

	@Override
	protected Composite createComposite(float alpha) {
		return new AndroidComposite(alpha);
	}

	@Override
	protected org.arakhne.afc.ui.vector.Paint createPaint(Object paintObject) {
		return new AndroidPaint((Paint)paintObject);
	}

	@Override
	protected Font createFont(String name, FontStyle style, float size) {
		return AndroidPaint.getFont(name, style, size, getCurrentDrawingContext());
	}

	@Override
	protected Font createFont(Object fontObject) {
		if (fontObject==null) return null;
		return new AndroidPaint((android.graphics.Paint)fontObject);
	}

	@Override
	protected Font getDefaultFont() {
		return AndroidPaint.getDefaultFont();
	}

	@Override
	protected FontMetrics createFontMetrics(Object metricsObject) {
		if (metricsObject==null) return null;
		return new AndroidPaint((android.graphics.Paint)metricsObject);
	}

	@Override
	protected FontMetrics createFontMetrics(Font font) {
		if (font instanceof FontMetrics)
			return (FontMetrics)font;
		return new AndroidPaint(toNativeUIObject(Paint.class, font));
	}

	@Override
	protected Image createImage(URL url) {
		if (url==null) return null;
		try {
			return createImage(url.openStream());
		}
		catch (IOException e) {
			throw new IOError(e);
		}
	}

	@Override
	protected Image createImage(InputStream stream) {
		if (stream==null) return null;
		AndroidImage img = new AndroidImage(stream);
		if (img.getBitmap()==null) return null;
		return img;
	}

	@Override
	protected Image createImage(int width, int height, boolean isAlpha) {
		AndroidImage img = new AndroidImage(width, height, isAlpha);
		if (img.getBitmap()==null) return null;
		return img;
	}

	@Override
	protected Image createImage(Object imageObject) {
		if (imageObject==null) return null;
		AndroidImage img = new AndroidImage((Bitmap)imageObject);
		if (img.getBitmap()==null) return null;
		return img;		
	}

	@Override
	protected Image createTransparentImage(Image imageObject, float transparency) {
		if (imageObject==null) return null;
		Bitmap aImg = toNativeUIObject(Bitmap.class, imageObject);
		Bitmap mutableBitmap = aImg.copy(Bitmap.Config.ARGB_8888, true);
		Canvas canvas = new Canvas(mutableBitmap);
		float t = 255f*(transparency+1f)/2f;
		int colour = ((int)t & 0xFF) << 24;
		canvas.drawColor(colour, PorterDuff.Mode.DST_OUT);
		return new AndroidImage(mutableBitmap);
	}

	@Override
	protected Image makeTransparentImage(Image imageObject, float transparency) {
		if (imageObject==null) return null;
		Bitmap aImg = toNativeUIObject(Bitmap.class, imageObject);
		Bitmap mutableBitmap = aImg.isMutable() ?
					aImg : aImg.copy(Bitmap.Config.ARGB_8888, true);
		Canvas canvas = new Canvas(mutableBitmap);
		float t = 255f*(transparency+1f)/2f;
		int colour = ((int)t & 0xFF) << 24;
		canvas.drawColor(colour, PorterDuff.Mode.DST_OUT);
		return new AndroidImage(mutableBitmap);
	}

	@Override
	protected void write(Image image, String type, OutputStream stream)
			throws IOException {
		String lt = type.toLowerCase();
		Bitmap aImg = toNativeUIObject(Bitmap.class, image);
		CompressFormat format;
		if (lt.equals("png")) { //$NON-NLS-1$
			format = CompressFormat.PNG;
		}
		else if (lt.equals("jpeg") || lt.equals("jpg")) { //$NON-NLS-1$ //$NON-NLS-2$
			format = CompressFormat.JPEG;
		}
		else if (lt.equals("webp")) { //$NON-NLS-1$
			format = CompressFormat.WEBP;
		}
		else {
			throw new IOException("Unsupported image type: "+type); //$NON-NLS-1$
		}
		if (!aImg.compress(format, 100, stream)) {
			throw new IOException("Unable to write the Android image for type: "+type); //$NON-NLS-1$
		}
	}

	@Override
	protected Stroke createStroke(float width, LineJoin join, EndCap endCap,
			float mitterLimit, float[] dashes, float dashPhase) {
		return new AndroidPaint(width, join, endCap, mitterLimit, dashes, dashPhase);
	}

	@Override
	protected Stroke createStroke(Object strokeObject) {
		if (strokeObject==null) return null;
		return new AndroidPaint((Paint)strokeObject);
	}

	@Override
	protected Color createColor(int red, int green, int blue, int alpha) {
		return new AndroidColor(red, green, blue, alpha);
	}

	@Override
	protected Color createColor(Object rawColorObject) {
		if (rawColorObject==null) return null;
		if (rawColorObject instanceof Drawable) {
			return new AndroidColor((Drawable)rawColorObject);
		}
		return new AndroidColor(((Number)rawColorObject).intValue());
	}

	@Override
	protected <T> T findObjectWithId(int id, Class<T> type) {
		Object o = null;
		if (id==0) {
			o = AndroidPaint.getDefaultFont();
		}
		if (o!=null && type.isInstance(o)) return type.cast(o);
		return null;
	}

	@Override
	protected Pdf wrapPdf(URL url) throws IOException {
		return new AndroidPdf(url);
	}

	@Override
	protected int HSBtoRGB(float hue, float saturation, float brightness) {
		float[] t = new float[] {hue, saturation, brightness};
		return android.graphics.Color.HSVToColor(t);
	}

	@Override
	protected Image createColorizedImage(Image imageObject,
			Color filtering_color, float alpha) {
		if (filtering_color==null || imageObject==null) return imageObject;
		Bitmap img = toNativeUIObject(Bitmap.class, imageObject);
		Bitmap mutableBitmap = img.copy(Bitmap.Config.ARGB_8888, true);
		int[] pixels = new int[mutableBitmap.getHeight()*mutableBitmap.getWidth()];
		mutableBitmap.getPixels(pixels, 0, mutableBitmap.getWidth(), 0, 0, mutableBitmap.getWidth(), mutableBitmap.getHeight());
        
        int color_r, color_g, color_b;
        
        for(int i=0; i<pixels.length; ++i) {
			color_r = ((pixels[i] >> 16) & 0xff); 
			color_g = ((pixels[i] >> 8) & 0xff); 
			color_b = (pixels[i] & 0xff);

			color_r = (int)((filtering_color.getRed() + color_r) * alpha);
			if (color_r<0) color_r = 0;
			if (color_r>255) color_r = 0;

			color_g = (int)((filtering_color.getGreen() + color_g) * alpha);
			if (color_g<0) color_g = 0;
			if (color_g>255) color_g = 0;

			color_b = (int)((filtering_color.getBlue() + color_b) * alpha);
			if (color_b<0) color_b = 0;
			if (color_b>255) color_b = 0;

			pixels[i] = (pixels[i] & 0xff000000) | (color_r << 16) | (color_g << 8) | color_b;
        }
        
        mutableBitmap.setPixels(pixels, 0, mutableBitmap.getWidth(), 0, 0, mutableBitmap.getWidth(), mutableBitmap.getHeight());
		return new AndroidImage(mutableBitmap);
	}

	@Override
	protected Color createSelectionBackground() {
		return color(0xFF9ccf00);
	}

	@Override
	protected Color createSelectionForeground() {
		return color(0xFF000000);
	}

}