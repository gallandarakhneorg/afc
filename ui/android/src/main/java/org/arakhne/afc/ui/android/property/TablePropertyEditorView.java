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
package org.arakhne.afc.ui.android.property;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.arakhne.afc.ui.android.R;
import org.arakhne.afc.ui.android.button.ColorButton;
import org.arakhne.afc.ui.vector.Color;
import org.arakhne.afc.ui.vector.Colors;
import org.arakhne.afc.ui.vector.VectorToolkit;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/** Abstract implementation of a property editor inside a fragment. 
 * 
 * @author $Author: galland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class TablePropertyEditorView extends PropertyEditorView {

	/** Replies the field type that is supporting the given Java type.
	 * 
	 * @param type
	 * @return the field type or <code>null</code>.
	 */
	protected static FieldType toFieldType(Class<?> type) {
		if (String.class.isAssignableFrom(type)) {
			return FieldType.STRING;
		}
		if (Long.class.isAssignableFrom(type)) {
			return FieldType.INTEGER;
		}
		if (Integer.class.isAssignableFrom(type)) {
			return FieldType.INTEGER;
		}
		if (Long.class.isAssignableFrom(type)) {
			return FieldType.INTEGER;
		}
		if (Number.class.isAssignableFrom(type)) {
			return FieldType.FLOAT;
		}
		if (Boolean.class.isAssignableFrom(type)) {
			return FieldType.BOOLEAN;
		}
		if (Color.class.isAssignableFrom(type)) {
			return FieldType.COLOR;
		}
		if (URL.class.isAssignableFrom(type)) {
			return FieldType.URL;
		}
		if (URI.class.isAssignableFrom(type)) {
			return FieldType.EMAIL;
		}
		if (Enum.class.isAssignableFrom(type)) {
			return FieldType.COMBO;
		}
		return null;
	}

	/** Extract a color from a property object.
	 * 
	 * @param obj
	 * @return the color
	 */
	public static Color toColor(Object obj) {
		if (obj instanceof Color) {
			return (Color)obj;
		}
		if (obj instanceof Number) {
			return VectorToolkit.color(((Number)obj).intValue());
		}
		return VectorToolkit.color(obj);
	}

	private TableLayout tableLayout;
	private final Map<String,FieldType> fieldTypes = new TreeMap<String,FieldType>();

	/** Create a view to edit properties with only the android
	 * application package and this ApkLib package in the list
	 * of package names.
	 * 
	 * @param context
	 */
	protected TablePropertyEditorView(Context context) {
		super(context);
	}

	/** Create a view to edit properties with a list of package names.
	 * <p>
	 * The list of package names is used to retreive the labels of the
	 * properties in the Android resources.
	 * <p>
	 * The application package is automatically added at the first position
	 * of the given list of package names. The package of this apklib
	 * is added at the end of the list.
	 * 
	 * @param context
	 * @param packageNames is the list of the package names from which 
	 * the label resources may be retreived.
	 */
	protected TablePropertyEditorView(Context context, List<String> packageNames) {
		super(context, packageNames);
	}

	@Override
	protected void onResetContentView() {
		if (this.tableLayout!=null) {
			this.tableLayout.removeAllViews();
		}
	}

	@Override
	protected View onCreateTopContentView() {
		this.tableLayout = new TableLayout(getContext());
		this.tableLayout.setOrientation(LinearLayout.VERTICAL);
		// Layout parameters
		TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		this.tableLayout.setLayoutParams(layoutParams);
		return this.tableLayout;
	}

	/** Replies the type of a field.
	 * 
	 * @param fieldId
	 * @return the type; or <code>null</code> if the field is unknown.
	 */
	protected final FieldType getFieldType(String fieldId) {
		return this.fieldTypes.get(fieldId);
	}

	/** Replies the text editor with the given id and of the given type.
	 * 
	 * @param fieldId is the identifier of the field.
	 * @return the view or <code>null</code>.
	 */
	protected final TextEditor getTextEditor(String fieldId) {
		View v = findViewWithTag(fieldId);
		if (v instanceof EditText) {
			return new TextEditor((EditText)v); 
		}
		if (v instanceof TextView) {
			return new TextEditor((TextView)v); 
		}
		return null;
	}

	/** Replies the boolean editor with the given id and of the given type.
	 * 
	 * @param fieldId is the identifier of the field.
	 * @return the view or <code>null</code>.
	 */
	protected final BooleanEditor getBooleanEditor(String fieldId) {
		View v = findViewWithTag(fieldId);
		if (v instanceof Switch) {
			return new BooleanEditor((Switch)v); 
		}
		if (v instanceof CheckBox) {
			return new BooleanEditor((CheckBox)v); 
		}
		if (v instanceof ImageView) {
			return new BooleanEditor((ImageView)v); 
		}
		return null;
	}

	/** Replies the color editor with the given id and of the given type.
	 * 
	 * @param fieldId is the identifier of the field.
	 * @return the view or <code>null</code>.
	 */
	protected final ColorEditor getColorEditor(String fieldId) {
		View v = findViewWithTag(fieldId);
		if (v instanceof ColorButton) {
			return new ColorEditor((ColorButton)v); 
		}
		if (v instanceof TextView) {
			return new ColorEditor((TextView)v); 
		}
		return null;
	}

	/** Replies the color editor with the given id and of the given type.
	 * 
	 * @param <T> is the type of the elements in the combo. 
	 * @param fieldId is the identifier of the field.
	 * @param type is the type of the elements in the combo. 
	 * @return the view or <code>null</code>.
	 */
	protected final <T> ComboEditor<T> getComboEditor(String fieldId, Class<T> type) {
		View v = findViewWithTag(fieldId);
		if (v instanceof ColorButton) {
			return new ComboEditor<T>((ColorButton)v); 
		}
		if (v instanceof TextView) {
			return new ComboEditor<T>((TextView)v); 
		}
		return null;
	}

	private TextView addLabel(TableRow row, String label) {
		TextView labelView = new TextView(getContext());
		labelView.setText(label);
		row.addView(labelView, new TableRow.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				1f));
		return labelView;
	}

	private TextView addLabel(TableRow row, String label, int style) {
		TextView labelView = new TextView(getContext());
		if (style!=0) labelView.setInputType(style);
		if (label!=null) labelView.setText(label);
		row.addView(labelView, new TableRow.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				1f));
		return labelView;
	}

	/** Add a field in the editor that permits to enter a string.
	 * 
	 * @param label is the label associated with the field.
	 * @param fieldId is the identifier of the field.
	 * @param value is the value in the field.
	 * @return the edition view.
	 */
	protected final TextEditor addStringField(String label, String fieldId, String value) {
		TextEditor editor;
		TableRow row = new TableRow(getContext());
		// Label
		addLabel(row, label);
		// Field
		if (isEditable()) {
			EditText editView = new EditText(getContext());
			editView.setTag(fieldId);
			editView.setText(value);
			row.addView(editView, new TableRow.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					1f));
			editor = new TextEditor(editView);
		}
		else {
			editor = new TextEditor(addLabel(row, value, 0));
		}

		this.tableLayout.addView(row, new TableRow.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		this.fieldTypes.put(fieldId, FieldType.STRING);

		return editor;
	}

	/** Add a field in the editor that permits to enter an integer.
	 * 
	 * @param label is the label associated with the field.
	 * @param fieldId is the identifier of the field.
	 * @param isPositiveOnly indicates if only the positives numbers are supported.
	 * @param value is the value in the field.
	 * @return the edition view.
	 */
	protected final TextEditor addIntegerField(String label, String fieldId, boolean isPositiveOnly, long value) {
		TextEditor editor;
		TableRow row = new TableRow(getContext());
		// Label
		addLabel(row, label);
		// Field
		int inputType = InputType.TYPE_CLASS_NUMBER;
		if (!isPositiveOnly) inputType |= InputType.TYPE_NUMBER_FLAG_SIGNED;
		if (isEditable()) {
			EditText editView = new EditText(getContext());
			editView.setTag(fieldId);
			editView.setInputType(inputType);
			editView.setText(Long.toString(
					isPositiveOnly ? Math.abs(value) : value));
			row.addView(editView, new TableRow.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					1f));
			editor = new TextEditor(editView);
		}
		else {
			editor = new TextEditor(addLabel(row,Long.toString(value), inputType));
		}

		this.tableLayout.addView(row, new TableRow.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		this.fieldTypes.put(fieldId, FieldType.INTEGER);

		return editor;
	}

	/** Add a field in the editor that permits to enter a floating-point number.
	 * 
	 * @param label is the label associated with the field.
	 * @param fieldId is the identifier of the field.
	 * @param isPositiveOnly indicates if only the positives numbers are supported.
	 * @param value is the value in the field.
	 * @return the edition view.
	 */
	protected final TextEditor addFloatField(String label, String fieldId, boolean isPositiveOnly, double value) {
		TextEditor editor;
		TableRow row = new TableRow(getContext());
		// Label
		addLabel(row, label);
		// Field
		int inputType = InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL;
		if (!isPositiveOnly) inputType |= InputType.TYPE_NUMBER_FLAG_SIGNED;
		if (isEditable()) {
			EditText editView = new EditText(getContext());
			editView.setTag(fieldId);
			editView.setInputType(inputType);
			editView.setText(Double.toString(
					isPositiveOnly ? Math.abs(value) : value));
			row.addView(editView, new TableRow.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					1f));			
			editor = new TextEditor(editView);
		}
		else {
			editor = new TextEditor(addLabel(row,Double.toString(value), inputType));
		}

		this.tableLayout.addView(row, new TableRow.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		this.fieldTypes.put(fieldId, FieldType.FLOAT);

		return editor;
	}

	/** Add a field in the editor that permits to enter a boolean value.
	 * 
	 * @param label is the label associated with the field.
	 * @param fieldId is the identifier of the field.
	 * @param value is the value in the field.
	 * @return the edition view.
	 */
	protected final BooleanEditor addBooleanField(String label, String fieldId, boolean value) {
		BooleanEditor editor;
		TableRow row = new TableRow(getContext());
		// Label
		addLabel(row, label);
		// Field
		if (isEditable()) {
			Switch editView = new Switch(getContext());
			editView.setTag(fieldId);
			editView.setChecked(value);
			row.addView(editView, new TableRow.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					1f));
			editor = new BooleanEditor(editView);
		}
		else {
			ImageView imageView = new ImageView(getContext());
			row.addView(imageView, new TableRow.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					1f));
			editor = new BooleanEditor(imageView);
			editor.setChecked(value);
		}

		this.tableLayout.addView(row, new TableRow.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		this.fieldTypes.put(fieldId, FieldType.BOOLEAN);

		return editor;
	}

	/** Add a field in the editor that permits to enter a textual password.
	 * 
	 * @param label is the label associated with the field.
	 * @param fieldId is the identifier of the field.
	 * @param value is the value in the field.
	 * @return the edition view.
	 */
	protected final TextEditor addTextPasswordField(String label, String fieldId, String value) {
		TextEditor editor;
		TableRow row = new TableRow(getContext());
		// Label
		addLabel(row, label);
		// Field
		int inputType =
				InputType.TYPE_CLASS_TEXT
				|InputType.TYPE_TEXT_VARIATION_PASSWORD;
		if (isEditable()) {
			EditText editView = new EditText(getContext());
			editView.setTag(fieldId);
			editView.setInputType(inputType);
			editView.setText(value);
			row.addView(editView, new TableRow.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					1f));
			editor = new TextEditor(editView);
		}
		else {
			editor = new TextEditor(addLabel(row, value, inputType));
		}

		this.tableLayout.addView(row, new TableRow.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		this.fieldTypes.put(fieldId, FieldType.PASSWORD);

		return editor;
	}

	/** Add a field in the editor that permits to enter a numeric password.
	 * 
	 * @param label is the label associated with the field.
	 * @param fieldId is the identifier of the field.
	 * @param value is the value in the field.
	 * @return the edition view.
	 */
	protected final TextEditor addNumPasswordField(String label, String fieldId, int value) {
		TextEditor editor;
		TableRow row = new TableRow(getContext());
		// Label
		addLabel(row, label);
		// Field
		int inputType = InputType.TYPE_CLASS_NUMBER
				|InputType.TYPE_NUMBER_VARIATION_PASSWORD;
		if (isEditable()) {
			EditText editView = new EditText(getContext());
			editView.setTag(fieldId);
			editView.setInputType(inputType);
			editView.setText(Integer.toString(value));
			row.addView(editView, new TableRow.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					1f));
			editor = new TextEditor(editView);
		}
		else {
			editor = new TextEditor(addLabel(row, Integer.toString(value), inputType));
		}

		this.tableLayout.addView(row, new TableRow.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		this.fieldTypes.put(fieldId, FieldType.PASSWORD);

		return editor;
	}

	/** Add a field in the editor that permits to enter an email.
	 * 
	 * @param label is the label associated with the field.
	 * @param fieldId is the identifier of the field.
	 * @param value is the value in the field.
	 * @return the edition view.
	 */
	protected final TextEditor addEmailField(String label, String fieldId, URI value) {
		TextEditor editor;
		TableRow row = new TableRow(getContext());
		// Label
		addLabel(row, label);
		// Field
		int inputType = InputType.TYPE_CLASS_TEXT
				|InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
		if (isEditable()) {
			EditText editView = new EditText(getContext());
			editView.setTag(fieldId);
			editView.setInputType(inputType);
			editView.setText(value.toString());
			row.addView(editView, new TableRow.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					1f));
			editor = new TextEditor(editView);
		}
		else {
			editor = new TextEditor(addLabel(row, value.toString(), inputType));
		}

		this.tableLayout.addView(row, new TableRow.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		this.fieldTypes.put(fieldId, FieldType.EMAIL);

		return editor;
	}

	/** Add a field in the editor that permits to enter an uri.
	 * 
	 * @param label is the label associated with the field.
	 * @param fieldId is the identifier of the field.
	 * @param value is the value in the field.
	 * @return the edition view.
	 */
	protected final TextEditor addUriField(String label, String fieldId, URL value) {
		TextEditor editor;
		TableRow row = new TableRow(getContext());
		// Label
		addLabel(row, label);
		// Field
		int inputType = InputType.TYPE_CLASS_TEXT
				|InputType.TYPE_TEXT_VARIATION_URI;
		if (isEditable()) {
			EditText editView = new EditText(getContext());
			editView.setTag(fieldId);
			editView.setInputType(inputType);
			if (value!=null) editView.setText(value.toExternalForm());
			row.addView(editView, new TableRow.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					1f));
			editor = new TextEditor(editView);
		}
		else {
			String v = null;
			if (value!=null) v = value.toExternalForm();
			editor = new TextEditor(addLabel(row, v, inputType));
		}

		this.tableLayout.addView(row, new TableRow.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		this.fieldTypes.put(fieldId, FieldType.URL);

		return editor;
	}

	/** Add a field in the editor that permits to enter a color.
	 * 
	 * @param label is the label associated with the field.
	 * @param fieldId is the identifier of the field.
	 * @param value is the value in the field.
	 * @return the edition view.
	 */
	protected final ColorEditor addColorField(String label, String fieldId, Color value) {
		ColorEditor editor;
		TableRow row = new TableRow(getContext());
		// Label
		addLabel(row, label);
		// Field
		if (isEditable()) {
			ColorButton editView = new ColorButton(getContext());
			editView.setTag(fieldId);
			editView.setColor(value==null ? null : value.getRGB());
			row.addView(editView, new TableRow.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					1f));
			editor = new ColorEditor(editView);
		}
		else {
			Color c = value==null ? Colors.BLACK : value;
			editor = new ColorEditor(addLabel(row, null, 0));
			editor.setColor(c);
		}

		this.tableLayout.addView(row, new TableRow.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		this.fieldTypes.put(fieldId, FieldType.COLOR);

		return editor;
	}

	/** Add a field in the editor that permits to enter a value with a combo.
	 * 
	 * @param <T> is the type of the choices.
	 * @param label is the label associated with the field.
	 * @param fieldId is the identifier of the field.
	 * @param choices are the possible choices in the combo.
	 * @param value is the value in the field.
	 * @return the edition view.
	 */
	protected final <T> ComboEditor<T> addComboField(String label, String fieldId, List<T> choices, T value) {
		ComboEditor<T> editor;
		TableRow row = new TableRow(getContext());
		// Label
		addLabel(row, label);
		if (isEditable()) {
			// Spinner
			Spinner editView = new Spinner(getContext());
			editView.setTag(fieldId);
			ArrayAdapter<T> adapter = new ArrayAdapter<T>(getContext(), android.R.layout.simple_spinner_item, choices);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			editView.setAdapter(adapter);
			row.addView(editView, new TableRow.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					1f));
			editor = new ComboEditor<T>(editView, adapter);
		}
		else {
			TextView textView = new TextView(getContext());
			textView.setTag(fieldId);
			row.addView(textView, new TableRow.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					1f));
			editor = new ComboEditor<T>(textView);
		}

		editor.setValue(value);

		this.tableLayout.addView(row, new TableRow.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		this.fieldTypes.put(fieldId, FieldType.COMBO);

		return editor;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Map<String,Object> getEditedProperties() {
		Map<String,Object> properties = new TreeMap<String,Object>();
		for(Entry<String,FieldType> entry : this.fieldTypes.entrySet()) {
			switch(entry.getValue()) {
			case BOOLEAN:
			{
				BooleanEditor editor = getBooleanEditor(entry.getKey());
				if (editor!=null) {
					properties.put(entry.getKey(), editor.isChecked());
				}
			}
			break;
			case STRING:
			case PASSWORD:
			{
				TextEditor editor = getTextEditor(entry.getKey());
				if (editor!=null) {
					properties.put(entry.getKey(), editor.getText());
				}
			}
			break;
			case EMAIL:
			{
				TextEditor editor = getTextEditor(entry.getKey());
				if (editor!=null) {
					try {
						properties.put(entry.getKey(), new URI(editor.getText()));
					}
					catch (URISyntaxException e) {
						Log.e(getClass().getName(), e.getLocalizedMessage(), e);
					}
				}
			}
			break;
			case URL:
			{
				TextEditor editor = getTextEditor(entry.getKey());
				if (editor!=null) {
					try {
						properties.put(entry.getKey(), new URL(editor.getText()));
					}
					catch (MalformedURLException e) {
						Log.e(getClass().getName(), e.getLocalizedMessage(), e);
					}
				}
			}
			break;
			case FLOAT:
			{
				TextEditor editor = getTextEditor(entry.getKey());
				if (editor!=null) {
					try {
						properties.put(entry.getKey(), Double.parseDouble(editor.getText()));
					}
					catch (Exception e) {
						Log.e(getClass().getName(), e.getLocalizedMessage(), e);
					}
				}
			}
			break;
			case INTEGER:
			{
				TextEditor editor = getTextEditor(entry.getKey());
				if (editor!=null) {
					try {
						properties.put(entry.getKey(), Long.parseLong(editor.getText()));
					}
					catch (Exception e) {
						Log.e(getClass().getName(), e.getLocalizedMessage(), e);
					}
				}
			}
			break;
			case COLOR:
			{
				ColorEditor editor = getColorEditor(entry.getKey());
				if (editor!=null) {
					properties.put(entry.getKey(), editor.getColor());
				}
			}
			break;
			case COMBO:
			{
				ComboEditor<?> editor = getComboEditor(entry.getKey(), Object.class);
				if (editor!=null) {
					Object value = editor.getValue();
					properties.put(entry.getKey(), value);
				}
			}
			break;
			default:
			}
		}
		return properties;
	}

	/** Wrapper to a TextEdit or a TextView depending the editable flag.
	 * 
	 * @author $Author: galland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	protected static class TextEditor {

		private final EditText edit;
		private final TextView view;


		/**
		 * @param edit
		 */
		public TextEditor(EditText edit) {
			this.edit = edit;
			this.view = null;
		}

		/**
		 * @param view
		 */
		public TextEditor(TextView view) {
			this.edit = null;
			this.view = view;
		}

		/** Change the text
		 * 
		 * @param text
		 */
		public void setText(String text) {
			if (this.edit!=null) {
				this.edit.setText(text);
			}
			else {
				this.view.setText(text);
			}
		}

		/** Replies the text.
		 * 
		 * @return the text.
		 */
		public String getText() {
			if (this.edit!=null) {
				return this.edit.getText().toString();
			}
			return this.view.getText().toString();
		}

	} // class TextEditor

	/** Wrapper to a CheckBox or a ImageView depending the editable flag.
	 * 
	 * @author $Author: galland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	protected static class BooleanEditor {

		private final CheckBox edit1;
		private final Switch edit2;
		private final ImageView view;
		private int resourceId = 0;


		/**
		 * @param edit
		 */
		public BooleanEditor(CheckBox edit) {
			this.edit1 = edit;
			this.edit2 = null;
			this.view = null;
		}

		/**
		 * @param edit
		 */
		public BooleanEditor(Switch edit) {
			this.edit1 = null;
			this.edit2 = edit;
			this.view = null;
		}

		/**
		 * @param view
		 */
		public BooleanEditor(ImageView view) {
			this.edit1 = null;
			this.edit2 = null;
			this.view = view;
		}

		/** Change the check
		 * 
		 * @param checked
		 */
		public void setChecked(boolean checked) {
			if (this.edit2!=null) {
				this.edit2.setChecked(checked);
			}
			else if (this.edit1!=null) {
				this.edit1.setChecked(checked);
			}
			else {
				this.resourceId = checked ?
						android.R.drawable.checkbox_on_background :
							android.R.drawable.checkbox_off_background;
				this.view.setImageResource(this.resourceId);
			}
		}

		/** Replies the check.
		 * 
		 * @return the check.
		 */
		public boolean isChecked() {
			if (this.edit2!=null) {
				return this.edit2.isChecked();
			}
			if (this.edit1!=null) {
				return this.edit1.isChecked();
			}
			return this.resourceId==android.R.drawable.checkbox_on_background;
		}

	} // class BooleanEditor

	/** Wrapper to a ColorButton or a TextView depending the editable flag.
	 * 
	 * @author $Author: galland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	protected static class ColorEditor {

		private final ColorButton edit;
		private final TextView view;
		private Color color = null;


		/**
		 * @param edit
		 */
		public ColorEditor(ColorButton edit) {
			this.edit = edit;
			this.view = null;
		}

		/**
		 * @param view
		 */
		public ColorEditor(TextView view) {
			this.edit = null;
			this.view = view;
		}

		/** Change the color
		 * 
		 * @param color
		 */
		public void setColor(Color color) {
			if (this.edit!=null) {
				if (color==null)
					this.edit.setColor((Integer)null);
				else
					this.edit.setColor(color.getRGB());
			}
			else {
				if (color==null) {
					this.view.setBackgroundResource(R.drawable.hatchs);
				}
				else {
					this.view.setBackgroundDrawable(new ColorDrawable(this.color.getRGB()));
				}
			}
		}

		/** Replies the color.
		 * 
		 * @return the color or <code>null</code> if default color.
		 */
		public Color getColor() {
			if (this.edit!=null) {
				Integer c = this.edit.getColor();
				if (c==null) return null;
				return VectorToolkit.color(c.intValue());
			}
			return this.color;
		}

	} // class ColorEditor

	/** Wrapper to a Spinner or a TextView depending the editable flag.
	 * 
	 * @param <T> is the type of the values in the combo.
	 * @author $Author: galland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	protected static class ComboEditor<T> {

		private final TextView view;
		private final ArrayAdapter<T> adapter;
		private final Spinner edit;
		private T value;


		/**
		 * @param view
		 */
		public ComboEditor(TextView view) {
			this.view = view;
			this.edit = null;
			this.adapter = null;
		}

		/**
		 * @param edit
		 * @param adapter
		 */
		public ComboEditor(Spinner edit, ArrayAdapter<T> adapter) {
			this.view = null;
			this.edit = edit;
			this.adapter = adapter;
			this.value = null;
		}

		/** Change the value
		 * 
		 * @param value
		 */
		public void setValue(T value) {
			if (value!=null) {
				if (this.edit!=null) {
					int pos = this.adapter.getPosition(value);
					if (pos>=0) {
						this.edit.setSelection(pos);
					}
				}
				else if (this.view!=null) {
					this.view.setText(value.toString());
				}
			}
		}

		/** Replies the value.
		 * 
		 * @return the value.
		 */
		@SuppressWarnings("unchecked")
		public T getValue() {
			if (this.edit!=null) {
				return (T)this.edit.getSelectedItem();
			}
			return this.value;
		}

	} // class ComboEditor

	/** Types of fields. 
	 * 
	 * @author $Author: galland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public enum FieldType {
		/** String. */
		STRING,
		/** Integer. */
		INTEGER,
		/** Float. */
		FLOAT,
		/** Boolean. */
		BOOLEAN,
		/** Color. */
		COLOR,
		/** Uri. */
		URL,
		/** Email. */
		EMAIL,
		/** Password. */
		PASSWORD,
		/** Combo. */
		COMBO;
	}

}
