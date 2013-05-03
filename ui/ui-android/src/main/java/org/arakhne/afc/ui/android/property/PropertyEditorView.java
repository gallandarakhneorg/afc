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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.arakhne.afc.ui.android.R;
import org.arakhne.afc.util.PropertyOwner;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/** Abstract implementation of a property editor inside a view.
 * <p>
 * This view create a set of fields in which the properties of
 * an object can be edited. This implementation does not make
 * any asssumption on the layout of the fields on the screen.
 * <p>
 * The labels of the properties, which are displayed on the screen,
 * are retreived from the resources of the Android application.
 * Let say the property <code>myProp</code>. This view tries
 * to retreive the entry <code>string/property_label_myProp</code>
 * in the resources. Because each entry in the resources is prefixed
 * by a package name, a list of package names to consider must be provided.
 * This list is given to the constructor of this view.
 * In addition to this list, this view search in the Android application
 * package (see {@link Context#getPackageName()}) and in the package of
 * this ApkLib (see {@link R}).   
 * 
 * @author $Author: galland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class PropertyEditorView extends LinearLayout {

	private final List<String> packageNames;
	private Collection<? extends PropertyOwner> editedObjects = null;
	private boolean isEditable = true;
	
	/** Create a view to edit properties with only the android
	 * application package and this ApkLib package in the list
	 * of package names.
	 * 
	 * @param context
	 */
	protected PropertyEditorView(Context context) {
		this(context, new ArrayList<String>(2));
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
	protected PropertyEditorView(Context context, List<String> packageNames) {
		super(context);
		this.packageNames = packageNames;
		this.packageNames.add(R.class.getPackage().getName()); // Add the package of this apklib
		this.packageNames.add(0, context.getPackageName()); // Force the use of the application package
		View top = onCreateTopContentView();
		if (top!=null) {
			addView(top);
		}
		if (this.editedObjects!=null) {
			onCreateFields(this.editedObjects);
		}
		LayoutParams layoutParams = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		setLayoutParams(layoutParams);
	}
	
	/** Replies the label 
	 * for the property with the given name.
	 * <p>
	 * This function search for a string in the Android resources
	 * with the name <code>"property_"+propertyName</code>.
	 * 
	 * @param propertyName is the name of the property.
	 * @return the label or <code>null</code>.
	 */
	protected String getPropertyLabel(String propertyName) {
		Resources r = getResources();
		String key = ":string/property_label_"+propertyName; //$NON-NLS-1$
		int id = 0;
		Iterator<String> packages = this.packageNames.iterator();
		while (packages.hasNext() && id==0) {
			id = r.getIdentifier(packages.next()+key, null, null);
		}
		String label = null;
		if (id!=0) {
			label = r.getString(id);
		}
		if (label==null || label.isEmpty()) {
			Log.d(getContext().getApplicationInfo().className,
					"Unable to retreive the resource"+key); //$NON-NLS-1$
			return "?"+key; //$NON-NLS-1$
		}
		return label;
	}

	/** Replies if the fragment enables to edit the properties.
	 * 
	 * @return <code>true</code> if the properties are editable.
	 */
	public boolean isEditable() {
		return this.isEditable;
	}

	/** Set if the fragment enables to edit the properties.
	 * 
	 * @param editable is <code>true</code> if the properties are editable.
	 */
	public void setEditable(boolean editable) {
		this.isEditable = editable;
	}

	/** Replies the edited objects.
	 * 
	 * @return the edited objects.
	 */
	public Collection<? extends PropertyOwner> getEditedObjects() {
		return this.editedObjects;
	}

	/** Change the edited object.
	 * 
	 * @param editedObjects
	 */
	public void setEditedObjects(Collection<? extends PropertyOwner> editedObjects) {
		if (this.editedObjects!=editedObjects) {
			if (this.editedObjects!=null) {
				onResetContentView();
			}
			this.editedObjects = editedObjects;
			if (this.editedObjects!=null) {
				onCreateFields(this.editedObjects);
			}
		}
	}

	/** Invoked when the content view must be cleared.
	 */
	protected abstract void onResetContentView();

	/** Invoked when the top-most (the root) content view must be initiated.
	 * @return the top view.
	 */
	protected abstract View onCreateTopContentView();

	/** Invoked to create the fields of the property editor.
	 * 
	 * @param editedObject is the edited object.
	 */
	protected abstract void onCreateFields(Collection<? extends PropertyOwner> editedObject);

	/** Replies the properties in the fragment.
	 * 
	 * @return the properties.
	 */
	public abstract Map<String,Object> getEditedProperties();
	
	/** Invoked by the API when the properties of an owner must be set.
	 * <p>
	 * This function shuold be overidden by subclasses to enable specific
	 * settings.
	 * 
	 * @param owner is the object to change.
	 * @param properties are the properties to set.
	 */
	@SuppressWarnings("static-method")
	public void setPropertiesOf(PropertyOwner owner, Map<String,Object> properties) {
		owner.setProperties(properties);
	}

}
