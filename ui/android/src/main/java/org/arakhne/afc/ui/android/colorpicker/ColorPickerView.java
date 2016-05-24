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

package org.arakhne.afc.ui.android.colorpicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.View;

/** A view that permits to pick a color.
 * This view is a panel on which all the colors are painted
 * and on which the user may click to pick a color.
 * <p>
 * The original source code was copied from
 * {@link "http://code.google.com/p/android-color-picker/"}.
 * Comments were added, and source code patched for
 * AFC compliance.
 * 
 * @author $Author: yukuku$
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see ColorPickerPreferenceView for a view inside the preference UI.
 * @deprecated see JavaFX API
 */
@Deprecated
public class ColorPickerView extends View {


	/** Current selected color.
	 */
	private final float[] hsv = { 1.f, 1.f, 1.f };

	private final Paint paint = new Paint();
	private Shader whiteBlackGradientShader = null;
	private Shader whiteColorGradientShader = null;
	private Shader composedGradientShader = null;

	/**
	 * @param context
	 * @param attrs
	 */
	public ColorPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public ColorPickerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private Shader getWhiteToBlackGradientShader() {
		if (this.whiteBlackGradientShader==null) {
			this.whiteBlackGradientShader = new LinearGradient(
					0.f, 0.f, 0.f, getMeasuredHeight(), 
					0xffffffff, 0xff000000, 
					TileMode.CLAMP);
		}
		return this.whiteBlackGradientShader;
	}

	private Shader getWhiteToColorGradientShader() {
		if (this.whiteColorGradientShader==null) {
			this.whiteColorGradientShader = new LinearGradient(
					0.f, 0.f, this.getMeasuredWidth(),
					0.f,
					0xffffffff,
					getRGB(),
					TileMode.CLAMP);
		}
		return this.whiteColorGradientShader;
	}

	private Shader getComposedGradientShader() {
		if (this.composedGradientShader==null) {
			this.composedGradientShader = new ComposeShader(
					getWhiteToBlackGradientShader(),
					getWhiteToColorGradientShader(), 
					PorterDuff.Mode.MULTIPLY);
		}
		return this.composedGradientShader;
	}

	private void reinitPaintingObjects() {
		this.whiteColorGradientShader = null;
		this.composedGradientShader = null;
		this.paint.setShader(getComposedGradientShader());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawRect(
				0.f, 0.f,
				this.getMeasuredWidth(), this.getMeasuredHeight(),
				this.paint);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// Ensure that the gradients will be correctly drawn according
		// to the size of the view
		reinitPaintingObjects();
	}

	/** Replies the RGB representation of the current color.
	 * 
	 * @return RGB
	 */
	public int getRGB() {
		return Color.HSVToColor(this.hsv);
	}

	/** Replies the HSV representation of the current color.
	 * 
	 * @return HSV
	 */
	public float[] getHSV() {
		return this.hsv.clone();
	}

	/** Set the hue of the color according to HSB color definition.
	 * 
	 * @param hue
	 */
	public void setHue(float hue) {
		if (this.hsv[0]!=hue) {
			this.hsv[0] = hue;
			reinitPaintingObjects();
			invalidate();
		}
	}
	
}
