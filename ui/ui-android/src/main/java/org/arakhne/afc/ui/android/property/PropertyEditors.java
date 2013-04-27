/* 
 * $Id$
 * 
 * Copyright (C) 2013 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
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

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.arakhne.afc.ui.android.R;
import org.arakhne.afc.ui.undo.AbstractUndoable;
import org.arakhne.afc.ui.undo.UndoManager;
import org.arakhne.afc.util.Pair;
import org.arakhne.afc.util.PropertyOwner;
import org.arakhne.util.ref.SoftValueHashMap;
import org.arakhne.vmutil.ReflectionUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;

/** Editors of properties. 
 * 
 * @author $Author: galland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PropertyEditors {

	/** Stored the mapping between type of object and type of property editor.
	 */
	private static final Map<Class<? extends PropertyOwner>,Class<? extends PropertyEditorView>> mapping = new HashMap<Class<? extends PropertyOwner>,Class<? extends PropertyEditorView>>();

	/** Use to accelerate the queries.
	 */
	private static final Map<Class<? extends PropertyOwner>,Class<? extends PropertyEditorView>> buffer = new SoftValueHashMap<Class<? extends PropertyOwner>, Class<? extends PropertyEditorView>>();


	/** Display the dialog that permits to edit the properties.
	 * 
	 * @param context
	 * @param isEditable indicates if the dialog box allows editions.
	 * @param undoManager is the undo manager to use.
	 * @param editedObjects are the edited objects.
	 */
	@SuppressWarnings("unchecked")
	public static void showDialog(final Context context, final boolean isEditable, final UndoManager undoManager, final Collection<? extends PropertyOwner> editedObjects) {
		//Detect the common type of the given objects
		Class<? extends PropertyOwner> type = null;
		if (!editedObjects.isEmpty()) {
			for(PropertyOwner obj : editedObjects) {
				if (type==null) {
					type = obj.getClass();
				}
				else {
					type = (Class<? extends PropertyOwner>)ReflectionUtil.getCommonType(type, obj.getClass());
				}
			}
		}

		// Create the views
		ScrollView scrollView = new ScrollView(context);
		LayoutParams layoutParams = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		scrollView.setLayoutParams(layoutParams);

		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setVerticalScrollBarEnabled(true);
		linearLayout.setHorizontalScrollBarEnabled(false);
		linearLayout.setScrollbarFadingEnabled(true);
		layoutParams = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		linearLayout.setLayoutParams(layoutParams);
		scrollView.addView(linearLayout);

		final PropertyEditorView fragment = PropertyEditors.createFragmentFor(context, type);
		fragment.setId(123456);
		fragment.setEditable(isEditable);
		fragment.setEditedObjects(editedObjects);
		linearLayout.addView(fragment);


		// Initialize the alert dialog builder
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.property_dialog_title);
		builder.setPositiveButton(
				android.R.string.ok,
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (isEditable) {
							Undo undo = new Undo(context.getString(R.string.undo_property_edition), editedObjects, fragment);
							undo.doEdit();
							if (undoManager!=null) {
								undoManager.add(undo);
							}
						}
					}
				});
		builder.setNegativeButton(android.R.string.cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
			}
		});
		builder.setView(scrollView);

		// Create and show the dialog
		AlertDialog alert = builder.create();
		alert.show();
	}

	/** Register a property editor fragment.
	 * 
	 * @param fragmentType is the type of the fragment to create for editing the properties of an object of the given type.
	 * @param objectType is the type of the object to edit.
	 */
	public static void registerEditorFragment(Class<? extends PropertyEditorView> fragmentType, Class<? extends PropertyOwner> objectType) {
		mapping.put(objectType, fragmentType);
	}

	/** Create a fragment that permits to edit the properties for
	 * the given object.
	 * 
	 * @param context
	 * @param editedObjectType
	 * @return a fragment or <code>null</code> if there is no known fragment.
	 */
	static PropertyEditorView createFragmentFor(Context context, Class<? extends PropertyOwner> editedObjectType) {
		assert(editedObjectType!=null);

		Class<?> objectType = editedObjectType;
		Class<? extends PropertyEditorView> fragmentType = buffer.get(objectType);
		while (fragmentType==null && objectType!=null) {
			fragmentType = mapping.get(objectType);
			if (fragmentType==null) {
				Class<?>[] interfaces = objectType.getInterfaces();
				int i=0;
				while (fragmentType==null && i<interfaces.length) {
					fragmentType = mapping.get(interfaces[i]);
					++i;
				}
			}
			objectType = objectType.getSuperclass();
		}
		if (fragmentType!=null) {
			try {
				Constructor<? extends PropertyEditorView> cons = fragmentType.getConstructor(Context.class);
				PropertyEditorView fragment = cons.newInstance(context);
				buffer.put(editedObjectType,fragmentType);
				return fragment;
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	/**
	 * @author $Author: galland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class Undo extends AbstractUndoable {

		private static final long serialVersionUID = -7759672095147438168L;

		private final String label;
		private final Collection<Pair<PropertyOwner,Map<String,Object>>> originalProperties = new ArrayList<Pair<PropertyOwner,Map<String,Object>>>();
		private final PropertyEditorView fragment;
		private final Map<String,Object> newProperties;

		/**
		 * @param label
		 * @param editedObjects
		 * @param fragment
		 */
		public Undo(String label, Collection<? extends PropertyOwner> editedObjects, PropertyEditorView fragment) {
			this.label = label;
			this.fragment = fragment;
			this.newProperties = this.fragment.getEditedProperties();
			for(PropertyOwner owner : editedObjects) {
				this.originalProperties.add(new Pair<PropertyOwner,Map<String,Object>>(
						owner,
						owner.getProperties()));
			}
		}

		@Override
		public String getPresentationName() {
			return this.label;
		}

		@Override
		protected void doEdit() {
			for(Pair<PropertyOwner,Map<String,Object>> object : this.originalProperties) {
				this.fragment.setPropertiesOf(object.getA(), this.newProperties);
			}
		}

		@Override
		protected void undoEdit() {
			for(Pair<PropertyOwner,Map<String,Object>> object : this.originalProperties) {
				this.fragment.setPropertiesOf(object.getA(), object.getB());
			}
		}

	}

}
