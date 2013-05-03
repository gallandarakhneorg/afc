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
 * @author $Author: galland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class FileListAdapter extends BaseAdapter {

	private List<File> files = new ArrayList<File>();

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
	 * @author $Author: galland$
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