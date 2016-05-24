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

package org.arakhne.afc.ui.android.filechooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.arakhne.afc.ui.android.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This class provides a drawing adapter for the elements
 * of the list in a file chooser.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
class FileListAdapter extends BaseAdapter {

	private List<File> files = new ArrayList<>();

	private final LayoutInflater layoutInflater;
	private final FileChooserIconSelector iconSelector;

	/**
	 * @param context is the drawing context.
	 * @param iconSelector is the selector of icon to use.
	 */
	public FileListAdapter(Context context, FileChooserIconSelector iconSelector) {
		this.layoutInflater = LayoutInflater.from(context);
		this.iconSelector = iconSelector;
	}
	
	/** Set the list of files.
	 * 
	 * @param list
	 */
	public void set(List<File> list) {
		this.files = list;
		notifyDataSetChanged();
	}
	
	/** Clear the list of files to display.
	 */
	public void clear() {
		this.files.clear();
		notifyDataSetChanged();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getCount() {
		return this.files.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getItem(int position) {
		return this.files.get(position);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder = null;

		if (row == null) {
			row = this.layoutInflater.inflate(R.layout.filechooser_onefile, parent, false);
			holder = new ViewHolder(row);
			row.setTag(holder);
		} else {
			// Reduce, reuse, recycle!
			holder = (ViewHolder) row.getTag();
		}

		// Get the file at the current position
		File file = getItem(position);

		// Set the TextView as the file name
		holder.nameView.setText(file.getName());

		// If the item is not a directory, use the file icon
		int icon;
		if (file.isDirectory()) {
			icon = R.drawable.ic_folder;
		}
		else {
			icon = R.drawable.ic_file;
			if (this.iconSelector!=null) {
				icon = this.iconSelector.selectIconFor(file, icon);
			}
		}
		
		holder.iconView.setImageResource(icon);

		return row;
	}

	/**
	 * This class provides a drawing adapter for the elements
	 * of the list in a file chooser.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class ViewHolder {
		
		public final TextView nameView;
		public final ImageView iconView;

		public ViewHolder(View row) {
			this.nameView = (TextView) row.findViewById(R.id.fileChooserFileName);
			this.iconView = (ImageView) row.findViewById(R.id.fileChooserFileIcon);
		}
		
	}
}