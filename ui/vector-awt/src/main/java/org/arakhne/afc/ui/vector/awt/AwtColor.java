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

import org.arakhne.afc.ui.vector.Color;

/** AWT implementation of the generic color.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
class AwtColor extends java.awt.Color implements Color, NativeWrapper {

	private static final long serialVersionUID = 6487706440529938798L;

	/**
	 * @param red
	 * @param green
	 * @param blue
	 * @param alpha
	 */
	public AwtColor(int red, int green, int blue, int alpha) {
		super(red, green, blue, alpha);
	}
	
	private AwtColor(java.awt.Color c) {
		super(c.getRGB(), true);
	}

	@Override
	public Color brighterColor() {
		return new AwtColor(brighter());
	}

	@Override
	public Color darkerColor() {
		return new AwtColor(darker());
	}
	
	@Override
	public Color transparentColor() {
		int alpha = getAlpha() / 2;
		return new AwtColor(
				getRed(), getGreen(), getBlue(),
				alpha);
	}
	
	@Override
	public <T> T getNativeObject(Class<T> type) {
		return type.cast(this);
	}

	@Override
	public float[] getHSB() {
		return RGBtoHSB(getRed(), getGreen(), getBlue(), null);
	}

	@Override
	public float getSaturation() {
		float[] t = RGBtoHSB(getRed(), getGreen(), getBlue(), null);
		return t[1];
	}

	@Override
	public float getBrightness() {
		float[] t = RGBtoHSB(getRed(), getGreen(), getBlue(), null);
		return t[2];
	}

	@Override
	public float getHue() {
		float[] t = RGBtoHSB(getRed(), getGreen(), getBlue(), null);
		return t[0];
	}

}