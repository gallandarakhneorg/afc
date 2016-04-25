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
import org.arakhne.afc.ui.android.colorpicker.ColorPickerDialog.OnColorPickerListener;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;

/** A preference that permits to pick a color in the preference UI.
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
public class ColorPickerPreference extends Preference {

	private int rgb;

	/**
	 * @param context
	 * @param attrs
	 */
	public ColorPickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWidgetLayoutResource(R.layout.colorpicker_pref_widget);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public ColorPickerPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setWidgetLayoutResource(R.layout.colorpicker_pref_widget);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onBindView(View view) {
		super.onBindView(view);

		// Set our custom views inside the layout
		View kotak = view.findViewById(R.id.colorpicker_pref_widget_kotak);
		if (kotak != null) {
			kotak.setBackgroundColor(this.rgb);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onClick() {
		OnColorPickerListener listener = new OnColorPickerListener() {
			@Override
			public void onColorPickingCanceled(ColorPickerDialog dialog) {
				//
			}
			@Override
			public void onColorPicked(ColorPickerDialog dialog, int color) {
				setRGB(color);
			}
			@Override
			public void onDefaultColorPicked(ColorPickerDialog dialog) {
				//
			}
		};
		
		new ColorPickerDialog(getContext(), this.rgb, false, listener).show();
	}
	
	/** Set the rgb preference after asking to the
	 * client if the change is allowed.
	 * 
	 * @param rgb
	 */
	public void setRGB(int rgb) {
		// They don't want the value to be set
		if (!callChangeListener(rgb)) return;
		ColorPickerPreference.this.rgb = rgb;
		persistInt(ColorPickerPreference.this.rgb);
		notifyChanged();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		// This preference type's value type is Integer,
		// so we read the default value from the attributes
		// as an Integer.
		return a.getInteger(index, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
		if (restoreValue) {
			// Restore state
			this.rgb = getPersistedInt(this.rgb);
		}
		else {
			// Set state
			this.rgb = ((Number)defaultValue).intValue();
			persistInt(this.rgb);
		}
	}

	/** {@inheritDoc}
	 * <p>
	 * Suppose a client uses this preference type without persisting. We
	 * must save the instance state so it is able to, for example, survive
	 * orientation changes.
	 */
	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();

		// No need to save instance state since it's persistent
		if (isPersistent())
			return superState;

		SavedState myState = new SavedState(superState, this.rgb);
		return myState;
	}

	@Override protected void onRestoreInstanceState(Parcelable state) {
		if (!state.getClass().equals(SavedState.class)) {
			// Didn't save state for us in onSaveInstanceState
			super.onRestoreInstanceState(state);
			return;
		}

		// Restore the instance state
		SavedState myState = (SavedState) state;
		super.onRestoreInstanceState(myState.getSuperState());
		this.rgb = myState.getRGB();
		notifyChanged();
	}

	/**
	 * SavedState, a subclass of {@link BaseSavedState}, will store the state
	 * of MyPreference, a subclass of Preference.
	 * <p>
	 * It is important to always call through to super methods.
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
	 */
	private static class SavedState extends BaseSavedState {
		
		private int rgb;

		/**
		 * @param source
		 */
		public SavedState(Parcel source) {
			super(source);
			this.rgb = source.readInt();
		}

		/**
		 * @param superState
		 * @param rgb
		 */
		public SavedState(Parcelable superState, int rgb) {
			super(superState);
			this.rgb = rgb;
		}
		
		/**
		 * Replies the saved RGB.
		 * 
		 * @return RGB
		 */
		public int getRGB() {
			return this.rgb;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(this.rgb);
		}

		@SuppressWarnings({ "unused", "hiding" })
		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
		
	}

}
