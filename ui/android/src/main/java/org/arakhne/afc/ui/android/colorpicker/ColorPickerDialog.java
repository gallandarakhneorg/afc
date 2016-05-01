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

import org.arakhne.afc.ui.android.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/** A dialog that permits to pick a color.
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
 * @deprecated see JavaFX API
 */
@Deprecated
public class ColorPickerDialog extends AlertDialog {

	private final OnColorPickerListener listener;
	private final float[] currentColorHsv = new float[3];

	private final View viewHue;
	private final ColorPickerView viewSatVal;
	private final ImageView viewCursor;
	private final View viewOldColor;
	private final View viewNewColor;
	private final ImageView viewTarget;
	private final ViewGroup viewContainer;

	/**
	 * Create a ColorPickerDialog with black color.
	 * Call this only from {@code OnCreateDialog()} or from a background thread.
	 * 
	 * @param context
	 * @param enableDefaultColorButton indicates if the button to select a default color
	 * is displayed or not. 
	 * @param listener is a listener on the picking events.
	 */
	public ColorPickerDialog(Context context, boolean enableDefaultColorButton, OnColorPickerListener listener) {
		this(context, 0xff000000, enableDefaultColorButton, listener);
	}

	/**
	 * Create a ColorPickerDialog.
	 * Call this only from {@code OnCreateDialog()} or from a background thread.
	 * 
	 * @param context
	 * @param color is the RGB color
	 * @param enableDefaultColorButton indicates if the button to select a default color
	 * is displayed or not. 
	 * @param listener is a listener on the picking events.
	 */
	public ColorPickerDialog(Context context, int color, boolean enableDefaultColorButton, OnColorPickerListener listener) {
		super(context);
		this.listener = listener;
		Color.colorToHSV(color, this.currentColorHsv);

		final View rootView = LayoutInflater.from(context).inflate(R.layout.colorpicker_dialog, null);
		this.viewHue = rootView.findViewById(R.id.colorpicker_viewHue);
		this.viewSatVal = (ColorPickerView) rootView.findViewById(R.id.colorpicker_viewSatBri);
		this.viewCursor = (ImageView) rootView.findViewById(R.id.colorpicker_cursor);
		this.viewOldColor = rootView.findViewById(R.id.colorpicker_warnaLama);
		this.viewNewColor = rootView.findViewById(R.id.colorpicker_warnaBaru);
		this.viewTarget = (ImageView) rootView.findViewById(R.id.colorpicker_target);
		this.viewContainer = (ViewGroup) rootView.findViewById(R.id.colorpicker_viewContainer);

		this.viewSatVal.setHue(getHue());
		this.viewOldColor.setBackgroundColor(color);
		this.viewNewColor.setBackgroundColor(color);

		this.viewHue.setOnTouchListener(new View.OnTouchListener() {
			@SuppressWarnings("synthetic-access")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE
						|| event.getAction() == MotionEvent.ACTION_DOWN
						|| event.getAction() == MotionEvent.ACTION_UP) {

					float y = event.getY();
					if (y < 0.f) y = 0.f;
					if (y > ColorPickerDialog.this.viewHue.getMeasuredHeight())
						y = ColorPickerDialog.this.viewHue.getMeasuredHeight() - 0.001f; // to avoid looping from end to start.
					float hue = 360.f - 360.f / ColorPickerDialog.this.viewHue.getMeasuredHeight() * y;
					if (hue == 360.f) hue = 0.f;

					setHue(hue);
					return true;
				}
				return false;
			}
		});

		this.viewSatVal.setOnTouchListener(new View.OnTouchListener() {
			@SuppressWarnings("synthetic-access")
			@Override public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE
						|| event.getAction() == MotionEvent.ACTION_DOWN
						|| event.getAction() == MotionEvent.ACTION_UP) {

					float x = event.getX(); // touch event are in dp units.
					float y = event.getY();

					if (x < 0.f) x = 0.f;
					if (x > ColorPickerDialog.this.viewSatVal.getMeasuredWidth())
						x = ColorPickerDialog.this.viewSatVal.getMeasuredWidth();
					if (y < 0.f) y = 0.f;
					if (y > ColorPickerDialog.this.viewSatVal.getMeasuredHeight())
						y = ColorPickerDialog.this.viewSatVal.getMeasuredHeight();

					setSaturationAndValue(
							1.f / ColorPickerDialog.this.viewSatVal.getMeasuredWidth() * x,
							1.f - (1.f / ColorPickerDialog.this.viewSatVal.getMeasuredHeight() * y));
					return true;
				}
				return false;
			}
		});

		setButton(BUTTON_POSITIVE, context.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				fireColorPicked();
			}
		});
		setButton(BUTTON_NEGATIVE, context.getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				fireColorPickingCanceled();
			}
		});
		setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface paramDialogInterface) {
				fireColorPickingCanceled();
			}
		});
		if (enableDefaultColorButton) {
			setButton(BUTTON_NEUTRAL,
				context.getString(R.string.default_color),
				new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					fireDefaultColorPicked();
				}
			});
		}
		// kill all padding from the dialog window
		setView(rootView, 0, 0, 0, 0);

		// move cursor & target on first draw
		ViewTreeObserver vto = rootView.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@SuppressWarnings("synthetic-access")
			@Override
			public void onGlobalLayout() {
				moveCursor();
				moveTarget();
				rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});
	}

	/** Notifies the listeners about the cancelation of the color picking.
	 */
	protected void fireColorPickingCanceled() {
		if (this.listener != null) {
			this.listener.onColorPickingCanceled(this);
		}
	}

	/** Notifies the listeners about the selection of the color.
	 */
	protected void fireColorPicked() {
		if (this.listener != null) {
			this.listener.onColorPicked(this, getRGB());
		}
	}

	/** Notifies the listeners about the selection of the default color.
	 */
	protected void fireDefaultColorPicked() {
		if (this.listener != null) {
			this.listener.onDefaultColorPicked(this);
		}
	}

	private void moveCursor() {
		float y = this.viewHue.getMeasuredHeight() - (getHue() * this.viewHue.getMeasuredHeight() / 360.f);
		if (y == this.viewHue.getMeasuredHeight()) y = 0.f;
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.viewCursor.getLayoutParams();
		layoutParams.leftMargin = (int) (this.viewHue.getLeft() - Math.floor(this.viewCursor.getMeasuredWidth() / 2) - this.viewContainer.getPaddingLeft());

		layoutParams.topMargin = (int) (this.viewHue.getTop() + y - Math.floor(this.viewCursor.getMeasuredHeight() / 2) - this.viewContainer.getPaddingTop());

		this.viewCursor.setLayoutParams(layoutParams);
	}

	private void moveTarget() {
		float x = getSaturation() * this.viewSatVal.getMeasuredWidth();
		float y = (1.f - getValue()) * this.viewSatVal.getMeasuredHeight();
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.viewTarget.getLayoutParams();
		layoutParams.leftMargin = (int) (this.viewSatVal.getLeft() + x - Math.floor(this.viewTarget.getMeasuredWidth() / 2) - this.viewContainer.getPaddingLeft());
		layoutParams.topMargin = (int) (this.viewSatVal.getTop() + y - Math.floor(this.viewTarget.getMeasuredHeight() / 2) - this.viewContainer.getPaddingTop());
		this.viewTarget.setLayoutParams(layoutParams);
	}

	/** Replies the RGB representation of the current selected color.
	 *  
	 * @return RGB
	 */
	public int getRGB() {
		return Color.HSVToColor(this.currentColorHsv);
	}

	/** Set the RGB representation of the current selected color.
	 *  
	 * @param rgb
	 */
	public void setRGB(int rgb) {
		float[] hsv = new float[3];
		Color.colorToHSV(rgb, hsv);
		setHSV(hsv);
	}

	/** Replies the HSV representation of the current selected color.
	 *  
	 * @return HSV
	 */
	public float[] getHSV() {
		return this.currentColorHsv.clone();
	}

	/** Set the HSV representation of the current selected color.
	 *  
	 * @param hsv
	 */
	public final void setHSV(float[] hsv) {
		setHSV(hsv[0], hsv[1], hsv[2]);
	}

	/** Set the HSV representation of the current selected color.
	 *  
	 * @param hue
	 * @param saturation
	 * @param value
	 */
	public void setHSV(float hue, float saturation, float value) {
		if (hue!=this.currentColorHsv[0]
			||saturation!=this.currentColorHsv[1]
			||value!=this.currentColorHsv[2]) {
			this.currentColorHsv[0] = hue;
			this.currentColorHsv[1] = saturation;
			this.currentColorHsv[2] = value;
			
			this.viewSatVal.setHue(getHue());
			moveCursor();
			moveTarget();
			this.viewNewColor.setBackgroundColor(getRGB());
		}
	}

	/** Replies the hue of the current color, according
	 * to the HSV representation.
	 * 
	 * @return the hue of the color.
	 */
	public float getHue() {
		return this.currentColorHsv[0];
	}

	/** Set the hue of the current color, according
	 * to the HSV representation.
	 * 
	 * @param hue the hue of the color.
	 */
	public void setHue(float hue) {
		if (hue!=this.currentColorHsv[0]) {
			this.currentColorHsv[0] = hue;
			this.viewSatVal.setHue(getHue());
			moveCursor();
			this.viewNewColor.setBackgroundColor(getRGB());
		}
	}

	/** Replies the saturation of the current color, according
	 * to the HSV representation.
	 * 
	 * @return the saturation of the color.
	 */
	public float getSaturation() {
		return this.currentColorHsv[1];
	}

	/** Set the saturation of the current color, according
	 * to the HSV representation.
	 * 
	 * @param sat is the saturation of the color.
	 */
	public void setSaturation(float sat) {
		if (sat!=this.currentColorHsv[1]) {
			this.currentColorHsv[1] = sat;
			moveTarget();
			this.viewNewColor.setBackgroundColor(getRGB());
		}
	}

	/** Replies the value of the current color, according
	 * to the HSV representation.
	 * 
	 * @return the value of the color.
	 */
	public float getValue() {
		return this.currentColorHsv[2];
	}

	/** Set the value of the current color, according
	 * to the HSV representation.
	 * 
	 * @param val is the value of the color.
	 */
	public void setValue(float val) {
		if (val!=this.currentColorHsv[2]) {
			this.currentColorHsv[2] = val;
			moveTarget();
			this.viewNewColor.setBackgroundColor(getRGB());
		}
	}

	/** Set the saturation and the value of the current color, according
	 * to the HSV representation.
	 * 
	 * @param sat is the saturation of the color.
	 * @param val is the value of the color.
	 */
	protected void setSaturationAndValue(float sat, float val) {
		if (sat!=this.currentColorHsv[1] || val!=this.currentColorHsv[2]) {
			this.currentColorHsv[1] = sat;
			this.currentColorHsv[2] = val;
			moveTarget();
			this.viewNewColor.setBackgroundColor(getRGB());
		}
	}

	/** Listener on color picking in a color picked
	 * dialog.
	 * 
	 * @author $Author: yukuku$
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public interface OnColorPickerListener {

		/** Invoked when the color picking was canceled.
		 * 
		 * @param dialog is the canceled dialog.
		 */
		public void onColorPickingCanceled(ColorPickerDialog dialog);

		/** Invoked when the color picking was successfull
		 * with a selected color.
		 * 
		 * @param dialog is the dialog from which the color was selected.
		 * @param color is the RGB color.
		 */
		public void onColorPicked(ColorPickerDialog dialog, int color);

		/** Invoked when the color picking was successfull
		 * with the default color.
		 * 
		 * @param dialog is the dialog from which the color was selected.
		 */
		public void onDefaultColorPicked(ColorPickerDialog dialog);

	}

}
