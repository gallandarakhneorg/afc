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