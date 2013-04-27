/* 
 * $Id$
 * 
 * Copyright (C) 2013 Stephane GALLAND.
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
package org.arakhne.afc.ui.vector.android;

import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.ui.vector.Font;
import org.arakhne.afc.ui.vector.FontMetrics;
import org.arakhne.afc.ui.vector.FontStyle;
import org.arakhne.afc.ui.vector.Stroke;
import org.arakhne.afc.ui.vector.VectorGraphics2D;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;

/** Android implementation of the generic stroke, font and font metrics.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class AndroidPaint implements Stroke, org.arakhne.afc.ui.vector.Paint, Font, FontMetrics, NativeWrapper {

	private static int toAndroid(FontStyle fs) {
		switch(fs) {
		case BOLD:
			return Typeface.BOLD;
		case BOLD_ITALIC:
			return Typeface.BOLD_ITALIC;
		case ITALIC:
			return Typeface.ITALIC;
		case PLAIN:
		default:
			return Typeface.NORMAL;
		}
	}

	private static final Paint defaultPaint = new Paint();

	/** Replies the default font.
	 * 
	 * @return a copy of the the default font.
	 */
	public static AndroidPaint getDefaultFont() {
		return new AndroidPaint(new Paint(defaultPaint));
	}

	/** Replies the font with the given attribute
	 * 
	 * @param name
	 * @param style
	 * @param size
	 * @param context
	 * @return the font.
	 */
	public static AndroidPaint getFont(String name, FontStyle style, float size, VectorGraphics2D context) {
		Paint pt = new Paint(((AndroidPaint)context.getFont()).getPaint());
		pt.setTypeface(Typeface.create(name, toAndroid(style)));
		pt.setTextSize(size);
		return new AndroidPaint(pt);
	}

	private final Paint paint;

	/**
	 * @param paint
	 */
	public AndroidPaint(Paint paint) {
		this.paint = paint;
	}

	/** Replies the painting context.
	 * 
	 * @return the painting context.
	 */
	public Paint getPaint() {
		return this.paint;
	}

	/**
	 * @param width
	 * @param join
	 * @param endCap
	 * @param mitterLimit
	 * @param dashes
	 * @param dashPhase
	 */
	public AndroidPaint(float width, LineJoin join, EndCap endCap,
			float mitterLimit, float[] dashes, float dashPhase) {
		this.paint = new Paint();
		this.paint.setStyle(Style.FILL_AND_STROKE);
		this.paint.setStrokeWidth(width);
		this.paint.setStrokeMiter(mitterLimit);
		switch(join) {
		case BEVEL:
			this.paint.setStrokeJoin(Join.BEVEL);
			break;
		case MITER:
			this.paint.setStrokeJoin(Join.MITER);
			break;
		case ROUND:
			this.paint.setStrokeJoin(Join.ROUND);
			break;
		default:
			throw new IllegalArgumentException("join"); //$NON-NLS-1$
		}
		switch(endCap) {
		case BUTT:
			this.paint.setStrokeCap(Cap.BUTT);
			break;
		case ROUND:
			this.paint.setStrokeCap(Cap.ROUND);
			break;
		case SQUARE:
			this.paint.setStrokeCap(Cap.SQUARE);
			break;
		default:
			throw new IllegalArgumentException("endCap"); //$NON-NLS-1$
		}
		if (dashes!=null && dashes.length>=2) {
			this.paint.setPathEffect(new DashPathEffect(dashes, dashPhase));
		}
	}

	@Override
	public <T> T getNativeObject(Class<T> type) {
		return type.cast(this.paint);
	}

	@Override
	public float[] getDashArray() {
		throw new UnsupportedOperationException();
	}

	@Override
	public float getDashPhase() {
		throw new UnsupportedOperationException();
	}

	@Override
	public float getLineWidth() {
		return this.paint.getStrokeWidth();
	}

	@Override
	public LineJoin getLineJoin() {
		switch(this.paint.getStrokeJoin()) {
		case BEVEL:
			return LineJoin.BEVEL;
		case MITER:
			return LineJoin.MITER;
		case ROUND:
			return LineJoin.ROUND;
		default:
			throw new IllegalStateException();
		}
	}

	@Override
	public EndCap getEndCap() {
		switch(this.paint.getStrokeCap()) {
		case BUTT:
			return EndCap.BUTT;
		case SQUARE:
			return EndCap.SQUARE;
		case ROUND:
			return EndCap.ROUND;
		default:
			throw new IllegalStateException();
		}
	}

	@Override
	public float getMiterLimit() {
		return this.paint.getStrokeMiter();
	}

	@Override
	public Font getFont() {
		return this;
	}

	@Override
	public float getLeading() {
		android.graphics.Paint.FontMetrics fm = this.paint.getFontMetrics();
		return fm.leading;
	}

	@Override
	public float getAscent() {
		android.graphics.Paint.FontMetrics fm = this.paint.getFontMetrics();
		return Math.abs(fm.ascent);
	}

	@Override
	public float getDescent() {
		android.graphics.Paint.FontMetrics fm = this.paint.getFontMetrics();
		return Math.abs(fm.descent);
	}

	@Override
	public float getHeight() {
		return this.paint.getTextSize();
	}

	@Override
	public float getMaxAscent() {
		android.graphics.Paint.FontMetrics fm = this.paint.getFontMetrics();
		return Math.abs(fm.top);
	}

	@Override
	public float getMaxDescent() {
		android.graphics.Paint.FontMetrics fm = this.paint.getFontMetrics();
		return Math.abs(fm.bottom);
	}

	@Override
	public float getMaxAdvance() {
		return this.paint.measureText("m"); //$NON-NLS-1$
	}

	@Override
	public float stringWidth(String str) {
		return this.paint.measureText(str);
	}

	@Override
	public Rectangle2f getStringBounds(String str) {
		Rect r = new Rect();
		this.paint.getTextBounds(str, 0, str.length(), r);
		return new Rectangle2f(0, 0, r.width(), r.height());
	}

	@Override
	public Rectangle2f getMaxCharBounds() {
		return new Rectangle2f(0, 0, getMaxAdvance(), getHeight());
	}

	/** Replies the preferred size of the font.
	 * 
	 * @return the size of the font; or {@code -1} if
	 * the size is unknown.
	 */
	@Override
	public float getSize() {
		return this.paint.getTextSize();
	}

	@Override
	public String getFamily() {
		return getName();
	}

	@Override
	public String getFontName() {
		return getPSName();
	}

	@Override
	public String getName() {
		Typeface tf = this.paint.getTypeface();
		tf = Typeface.create(tf, Typeface.NORMAL);
		if (tf.equals(Typeface.DEFAULT)) {
			return "Normal"; //$NON-NLS-1$
		}
		if (tf.equals(Typeface.MONOSPACE)) {
			return "Monospace"; //$NON-NLS-1$
		}
		if (tf.equals(Typeface.SANS_SERIF)) {
			return "Sans"; //$NON-NLS-1$
		}
		if (tf.equals(Typeface.SERIF)) {
			return "Serif"; //$NON-NLS-1$
		}
		return "Unknown"; //$NON-NLS-1$
	}

	@Override
	public String getPSName() {
		StringBuilder b = new StringBuilder();
		b.append(getName());
		b.append("."); //$NON-NLS-1$
		Typeface tf = this.paint.getTypeface();
		if (tf==null) tf = Typeface.DEFAULT;
		switch(tf.getStyle()) {
		case Typeface.BOLD_ITALIC:
			b.append("bolditalic"); //$NON-NLS-1$
			break;
		case Typeface.BOLD:
			b.append("bold"); //$NON-NLS-1$
			break;
		case Typeface.ITALIC:
			b.append("italic"); //$NON-NLS-1$
			break;
		default:
		case Typeface.NORMAL:
			b.append("plain"); //$NON-NLS-1$
			break;
		}
		return b.toString();
	}

	@Override
	public boolean isPlain() {
		Typeface tf = this.paint.getTypeface();
		if (tf==null) tf = Typeface.DEFAULT;
		return !tf.isBold() && !tf.isItalic();
	}

	@Override
	public boolean isBold() {
		Typeface tf = this.paint.getTypeface();
		if (tf==null) tf = Typeface.DEFAULT;
		return tf.isBold();
	}

	@Override
	public boolean isItalic() {
		Typeface tf = this.paint.getTypeface();
		if (tf==null) tf = Typeface.DEFAULT;
		return tf.isItalic();
	}

	@Override
	public Font deriveFont(float size) {
		Paint pt = new Paint(this.paint);
		pt.setTextSize(size);
		return new AndroidPaint(pt);
	}

	@Override
	public Font deriveFont(FontStyle style, float size) {
		Paint pt = new Paint(this.paint);
		pt.setTextSize(size);
		pt.setTypeface(Typeface.create(pt.getTypeface(), toAndroid(style)));
		return new AndroidPaint(pt);
	}

	@Override
	public Font deriveFont(FontStyle style) {
		Paint pt = new Paint(this.paint);
		pt.setTypeface(Typeface.create(pt.getTypeface(), toAndroid(style)));
		return new AndroidPaint(pt);
	}

	@Override
	public float getItalicAngle() {
		return 0;
	}

	@Override
	public String toString() {
		if (this.paint==null) return null;
		return "color=0x"+Integer.toHexString(this.paint.getColor()); //$NON-NLS-1$
	}

}