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

package org.arakhne.afc.ui.android.button;

import org.arakhne.afc.ui.android.R;
import org.arakhne.afc.ui.android.colorpicker.ColorPickerDialog;
import org.arakhne.afc.ui.android.colorpicker.ColorPickerDialog.OnColorPickerListener;
import org.arakhne.afc.util.ListenerCollection;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;


/** A button for picking a color.  
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public class ColorButton extends Button {

	private final Listener eventListener = new Listener();
	private final ListenerCollection<ColorChangeListener> listeners = new ListenerCollection<>();
	private Integer color = null;
	private boolean enableDefaultColor = true;

	/**
	 * @param context
	 */
	public ColorButton(Context context) {
		super(context, null, android.R.attr.buttonStyleSmall);
		setClickable(true);
		setOnClickListener(this.eventListener);
		setBackgroundResource(R.drawable.hatchs);
		setText(R.string.default_color);
	}

	/** Replies if the default color is allowed in this color button.
	 * 
	 * @return the default color is allowed in this color button.
	 */
	public boolean isDefaultColorEnabled() {
		return this.enableDefaultColor;
	}

	/** Set if the default color is allowed in this color button.
	 * 
	 * @param enable is the default color is allowed in this color button.
	 */
	public void setDefaultColorEnabled(boolean enable) {
		if (this.enableDefaultColor!=enable) {
			this.enableDefaultColor = enable;
			if (!this.enableDefaultColor && this.color==null) {
				setColor(Color.BLACK);
			}
		}
	}

	/** Change the selected color.
	 * 
	 * @param rgb
	 */
	public final void setColor(int rgb) {
		setColor(Integer.valueOf(rgb));
	}

	/** Change the selected color.
	 * 
	 * @param rgb
	 */
	public final void setColor(Integer rgb) {
		if ((rgb!=null || isDefaultColorEnabled())
				&&((this.color==null && rgb!=null)
				   || (this.color!=null && !this.color.equals(rgb)))) {
			this.color = rgb;
			if (this.color!=null) {
				setBackgroundColor(this.color.intValue());
				setText(this.color.toString());
			}
			else {
				setBackgroundResource(R.drawable.hatchs);
				setText(R.string.default_color);
			}
			fireColorChange(this.color);
		}
	}

	/** Replies the selected color.
	 * 
	 * @return the selected color; or <code>null</code> if the
	 * default color is selected.
	 */
	public Integer getColor() {
		return this.color;
	}

	/** Add listener on color changes.
	 * 
	 * @param listener
	 */
	public void addColorChangeListener(ColorChangeListener listener) {
		this.listeners.add(ColorChangeListener.class, listener);
	}

	/** Remove listener on color changes.
	 * 
	 * @param listener
	 */
	public void removeColorChangeListener(ColorChangeListener listener) {
		this.listeners.remove(ColorChangeListener.class, listener);
	}

	/** Notifies listeners about a color change.
	 * 
	 * @param color
	 */
	public void fireColorChange(int color) {
		for(ColorChangeListener listener : this.listeners.getListeners(ColorChangeListener.class)) {
			listener.onSelectedColorChanged(this, color);
		}
	}

	/**
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class Listener implements OnClickListener, OnColorPickerListener {

		/**
		 */
		public Listener() {
			//
		}

		@Override
		public void onClick(View v) {
			Integer color = getColor();
			if (color==null) {
				color = Color.BLACK;
			}
			ColorPickerDialog dialog;
			if (color!=null) { 
				dialog = new ColorPickerDialog(
						getContext(),
						color,
						isDefaultColorEnabled(),
						this);
			}
			else {
				dialog = new ColorPickerDialog(
						getContext(),
						isDefaultColorEnabled(),
						this);
			}
			dialog.show();
		}

		@Override
		public void onColorPickingCanceled(ColorPickerDialog dialog) {
			//
		}

		@Override
		public void onColorPicked(ColorPickerDialog dialog, int color) {
			setColor(color);
		}

		@Override
		public void onDefaultColorPicked(ColorPickerDialog dialog) {
			setColor(null);
		}

	}

}
