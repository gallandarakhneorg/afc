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
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
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
		String key = ":string/property_label_"+propertyName; 
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
					"Unable to retreive the resource"+key); 
			return "?"+key; 
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
