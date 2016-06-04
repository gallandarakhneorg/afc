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

package org.arakhne.afc.ui.vector.android;

import java.lang.ref.SoftReference;

import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.math.continous.object2d.Shape2f;
import org.arakhne.afc.ui.vector.Font;
import org.arakhne.afc.ui.vector.FontMetrics;
import org.arakhne.afc.ui.vector.FontStyle;
import org.arakhne.afc.ui.vector.GlyphList;
import org.arakhne.afc.ui.vector.Stroke;
import org.arakhne.afc.ui.vector.VectorGraphics2D;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;

/** Android implementation of the generic stroke, font and font metrics.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
class AndroidPaint implements Stroke, org.arakhne.afc.ui.vector.Paint, Font, FontMetrics, NativeWrapper, Cloneable {

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

	private Paint paint;

	/**
	 * @param paint
	 */
	public AndroidPaint(Paint paint) {
		assert(paint!=null);
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
			throw new IllegalArgumentException("join"); 
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
			throw new IllegalArgumentException("endCap"); 
		}
		if (dashes!=null && dashes.length>=2) {
			this.paint.setPathEffect(new DashPathEffect(dashes, dashPhase));
		}
	}

	@Override
	public AndroidPaint clone() {
		try {
			AndroidPaint p = (AndroidPaint)super.clone();
			p.paint = new Paint(this.paint);
			return p;
		}
		catch (CloneNotSupportedException e) {
			throw new Error(e);
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
		return this.paint.measureText("m"); 
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
			return "Normal"; 
		}
		if (tf.equals(Typeface.MONOSPACE)) {
			return "Monospace"; 
		}
		if (tf.equals(Typeface.SANS_SERIF)) {
			return "Sans"; 
		}
		if (tf.equals(Typeface.SERIF)) {
			return "Serif"; 
		}
		return "Unknown"; 
	}

	@Override
	public String getPSName() {
		return getPhysicalPSName();
	}

	@Override
	public String getPhysicalPSName() {
		StringBuilder b = new StringBuilder();
		b.append(getName());
		b.append("."); 
		Typeface tf = this.paint.getTypeface();
		if (tf==null) tf = Typeface.DEFAULT;
		switch(tf.getStyle()) {
		case Typeface.BOLD_ITALIC:
			b.append("bolditalic"); 
			break;
		case Typeface.BOLD:
			b.append("bold"); 
			break;
		case Typeface.ITALIC:
			b.append("italic"); 
			break;
		default:
		case Typeface.NORMAL:
			b.append("plain"); 
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
		return "color=0x"+Integer.toHexString(this.paint.getColor()); 
	}

	@Override
	public GlyphList createGlyphList(VectorGraphics2D g, char... characters) {
		return new AndroidGlyphList(characters);
	}

	@Override
	public GlyphList createGlyphList(VectorGraphics2D g, String text) {
		return new AndroidGlyphList(text);
	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class AndroidGlyphList implements GlyphList {

		private final char[] characters;
		private SoftReference<float[]> widths = null;
		private SoftReference<AndroidPath> globalPath = null;
		private SoftReference<Rectangle2f> globalBounds = null;
		
		/**
		 * @param characters
		 */
		public AndroidGlyphList(char... characters) {
			this.characters = characters;
		}

		/**
		 * @param text
		 */
		public AndroidGlyphList(String text) {
			this.characters = text.toCharArray();
		}

		@Override
		public int size() {
			return this.characters.length;
		}

		@Override
		public Font getFont() {
			return AndroidPaint.this.getFont();
		}

		@Override
		public char getCharAt(int index) {
			return this.characters[index];
		}

		@Override
		public float getWidthAt(int index) {
			float[] tab = (this.widths==null) ? null : this.widths.get();
			if (tab==null) {
				tab = new float[this.characters.length];
				Paint paint = getPaint();
				paint.getTextWidths(this.characters, 0, tab.length, tab);
				this.widths = new SoftReference<>(tab);
			}
			return tab[index];
		}

		@Override
		public Shape2f getOutlineAt(int index) {
			Path path = new Path();
			Paint paint = getPaint();
			paint.getTextPath(this.characters, index, 1, 0, 0, path);
			return new AndroidPath(path);
		}

		@Override
		public Shape2f getOutlineAt(int index, float x, float y) {
			Path path = new Path();
			Paint paint = getPaint();
			paint.getTextPath(this.characters, index, 1, x, y, path);
			return new AndroidPath(path);
		}

		@Override
		public Shape2f getOutline() {
			AndroidPath p = (this.globalPath==null) ? null : this.globalPath.get();
			if (p==null) {
				Path path = new Path();
				Paint paint = getPaint();
				paint.getTextPath(this.characters, 0, this.characters.length, 0, 0, path);
				p = new AndroidPath(path);
				this.globalPath = new SoftReference<>(p);
			}
			return p;
		}

		@Override
		public Shape2f getOutline(float x, float y) {
			Path path = new Path();
			Paint paint = getPaint();
			paint.getTextPath(this.characters, 0, this.characters.length, 0, 0, path);
			return new AndroidPath(path);
		}

		@Override
		public Rectangle2f getBoundsAt(int index) {
			Rect rect = new Rect();
			Paint paint = getPaint();
			paint.getTextBounds(this.characters, index, 1, rect);
			return new Rectangle2f(
					rect.left, rect.top,
					rect.width(), rect.height());
		}

		@Override
		public Rectangle2f getBoundsAt(int index, float x, float y) {
			Rect rect = new Rect();
			Paint paint = getPaint();
			paint.getTextBounds(this.characters, index, 1, rect);
			return new Rectangle2f(
					rect.left + x, rect.top + y,
					rect.width(), rect.height());
		}

		@Override
		public Rectangle2f getBounds() {
			Rectangle2f p = (this.globalBounds==null) ? null : this.globalBounds.get();
			if (p==null) {
				Rect rect = new Rect();
				Paint paint = getPaint();
				paint.getTextBounds(this.characters, 0, this.characters.length, rect);
				p = new Rectangle2f(
						rect.left, rect.top,
						rect.width(), rect.height());
				this.globalBounds = new SoftReference<>(p);
			}
			return p;
		}

		@Override
		public Rectangle2f getBounds(float x, float y) {
			Rect rect = new Rect();
			Paint paint = getPaint();
			paint.getTextBounds(this.characters, 0, this.characters.length, rect);
			return new Rectangle2f(
					rect.left + x, rect.top + y,
					rect.width(), rect.height());
			
		}
		
	} // class AndroidGlyphList
	
}