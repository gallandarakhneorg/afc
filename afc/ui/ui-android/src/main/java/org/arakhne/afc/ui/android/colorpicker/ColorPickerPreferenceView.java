/*
 * $Id$
 * 
 * Copyright 2012, Yuku Sugianto
 * Copyright 2013, Yuku Sugianto, Stephane Galland
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
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
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

/** A view that permits to pick a color in the preference UI.
 * <p>
 * The original source code was copied from
 * {@link "http://code.google.com/p/android-color-picker/"}.
 * Comments were added, and source code patched for
 * AFC compliance.
 * 
 * @author $Author: yukuku$
 * @author $Author: galland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see ColorPickerView for a view outside the preference UI.
 */
public class ColorPickerPreferenceView extends View {
	
	private final Paint paint;
	private final float rectSize;
	private final float strokeWidth;

	/**
	 * @param context
	 * @param attrs
	 */
	public ColorPickerPreferenceView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public ColorPickerPreferenceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		float density = context.getResources().getDisplayMetrics().density;
		this.rectSize = (float)Math.floor(24.f * density + 0.5f);
		this.strokeWidth = (float)Math.floor(1.f * density + 0.5f);

		this.paint = new Paint();
		this.paint.setColor(0xffffffff);
		this.paint.setStyle(Style.STROKE);
		this.paint.setStrokeWidth(this.strokeWidth);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawRect(
				this.strokeWidth, this.strokeWidth,
				this.rectSize - this.strokeWidth,
				this.rectSize - this.strokeWidth,
				this.paint);
	}
}
