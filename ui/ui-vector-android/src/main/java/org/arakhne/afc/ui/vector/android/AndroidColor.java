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

import org.arakhne.afc.ui.vector.Color;

import android.graphics.drawable.Drawable;

/** Android implementation of the generic color.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class AndroidColor implements Color, NativeWrapper {

	private static final long serialVersionUID = -3729571234595878175L;

	private static final double FACTOR = 0.7;

	private final int original;
	private final Drawable originalDrawable;

	/**
	 * @param color
	 */
	public AndroidColor(int color) {
		this.original = color;
		this.originalDrawable = null;
	}

	/**
	 * @param drawable
	 */
	public AndroidColor(Drawable drawable) {
		this.original = -1;
		this.originalDrawable = drawable;
	}

	/**
	 * @param red
	 * @param green
	 * @param blue
	 * @param alpha
	 */
	public AndroidColor(int red, int green, int blue, int alpha) {
		this.original = android.graphics.Color.argb(alpha, red, green, blue);
		this.originalDrawable = null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Color) {
			return getRGB() == ((Color)obj).getRGB();
		}
		if (obj instanceof Number) {
			return getRGB() == ((Number)obj).intValue();
		}
		if (obj instanceof Drawable) {
			return ((Drawable)obj).equals(this.originalDrawable);
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		if (this.originalDrawable!=null)
			return this.originalDrawable.hashCode(); 
		return this.original;
	}
	
	@Override
	public <T> T getNativeObject(Class<T> type) {
		if (this.originalDrawable!=null && type.isAssignableFrom(Drawable.class)) {
			return type.cast(this.originalDrawable);
		}
		return type.cast(this.original);
	}

	@Override
	public int getGreen() {
		if (this.originalDrawable!=null)
			throw new IllegalStateException("color is a Drawable"); //$NON-NLS-1$
		return android.graphics.Color.green(this.original);
	}

	@Override
	public int getRed() {
		if (this.originalDrawable!=null)
			throw new IllegalStateException("color is a Drawable"); //$NON-NLS-1$
		return android.graphics.Color.red(this.original);
	}

	@Override
	public int getBlue() {
		if (this.originalDrawable!=null)
			throw new IllegalStateException("color is a Drawable"); //$NON-NLS-1$
		return android.graphics.Color.blue(this.original);
	}

	@Override
	public int getAlpha() {
		if (this.originalDrawable!=null)
			throw new IllegalStateException("color is a Drawable"); //$NON-NLS-1$
		return android.graphics.Color.alpha(this.original);
	}

	@Override
	public int getRGB() {
		if (this.originalDrawable!=null)
			throw new IllegalStateException("color is a Drawable"); //$NON-NLS-1$
		return this.original;
	}

	@Override
	public Color brighterColor() {
		if (this.originalDrawable!=null)
			throw new IllegalStateException("color is a Drawable"); //$NON-NLS-1$
		int r = getRed();
		int g = getGreen();
		int b = getBlue();
		int alpha = getAlpha();

		/* From 2D group:
		 * 1. black.brighter() should return grey
		 * 2. applying brighter to blue will always return blue, brighter
		 * 3. non pure color (non zero rgb) will eventually return white
		 */
		int i = (int)(1.0/(1.0-FACTOR));
		if ( r == 0 && g == 0 && b == 0) {
			return new AndroidColor(i, i, i, alpha);
		}
		if ( r > 0 && r < i ) r = i;
		if ( g > 0 && g < i ) g = i;
		if ( b > 0 && b < i ) b = i;

		return new AndroidColor(
				Math.min((int)(r/FACTOR), 255),
				Math.min((int)(g/FACTOR), 255),
				Math.min((int)(b/FACTOR), 255),
				alpha);
	}

	@Override
	public Color darkerColor() {
		if (this.originalDrawable!=null)
			throw new IllegalStateException("color is a Drawable"); //$NON-NLS-1$
		return new AndroidColor(
				Math.max((int)(getRed()  *FACTOR), 0),
				Math.max((int)(getGreen()*FACTOR), 0),
				Math.max((int)(getBlue() *FACTOR), 0),
				getAlpha());
	}

	@Override
	public Color transparentColor() {
		if (this.originalDrawable!=null)
			throw new IllegalStateException("color is a Drawable"); //$NON-NLS-1$
		int alpha = getAlpha() / 2;
		return new AndroidColor(
				getRed(), getGreen(), getBlue(),
				alpha);
	}

	@Override
	public String toString() {
		if (this.originalDrawable!=null)
			return this.originalDrawable.toString();
		return Integer.toHexString(this.original);
	}

	@Override
	public float[] getHSB() {
		float[] t = new float[3];
		android.graphics.Color.RGBToHSV(getRed(), getGreen(), getBlue(), t);
		return t;
	}

	@Override
	public float getSaturation() {
		float[] t = new float[3];
		android.graphics.Color.RGBToHSV(getRed(), getGreen(), getBlue(), t);
		return t[1];
	}

	@Override
	public float getBrightness() {
		float[] t = new float[3];
		android.graphics.Color.RGBToHSV(getRed(), getGreen(), getBlue(), t);
		return t[2];
	}

	@Override
	public float getHue() {
		float[] t = new float[3];
		android.graphics.Color.RGBToHSV(getRed(), getGreen(), getBlue(), t);
		return t[0];
	}

}