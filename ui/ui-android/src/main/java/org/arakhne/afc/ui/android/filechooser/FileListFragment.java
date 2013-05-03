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
import java.io.FileFilter;
import java.util.List;

import org.arakhne.afc.ui.android.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

/**
 * Fragment that displays a list of Files in a given path.
 * <p>
 * The path to open at startup should be stored in the
 * argument with the name "path".
 * 
 * @author $Author: galland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class FileListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<File>> {

	private FileFilter fileFilter = null;
	private FileChooserIconSelector iconSelector = null;
	private FileListAdapter adapter = null;
	private File path;

	/**
	 * @param path is the he absolute path of the file (directory) to display.
	 * @param fileFilter is the file filter to use.
	 * @param iconSelector is the selector of icon.
	 * @return the new instance.
	 */
	public static FileListFragment newInstance(File path, FileFilter fileFilter, FileChooserIconSelector iconSelector) {
		FileListFragment fragment = new FileListFragment();
		Bundle args = new Bundle();
		if (path!=null) {
			args.putString(FileChooserActivity.SAVED_PATH_NAME, path.getAbsolutePath());
		}
		fragment.setArguments(args);
		fragment.setFileFilter(fileFilter);
		fragment.setIconSelector(iconSelector);
		return fragment;
	}

	/**
	 */
	public FileListFragment() {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.adapter = new FileListAdapter(getActivity(), this.iconSelector);
		this.path = Environment.getExternalStorageDirectory();

		Bundle args = getArguments();
		if (args!=null) {
			String absPath = args.getString(FileChooserActivity.SAVED_PATH_NAME);
			if (absPath!=null) {
				this.path = new File(absPath);
			}
		}
	}

	/**
	 * Set the filte filter to use.
	 * 
	 * @param fileFilter
	 */
	public void setFileFilter(FileFilter fileFilter) {
		this.fileFilter = fileFilter;
	}

	/**
	 * Set the icon selector to use.
	 * 
	 * @param iconSelector
	 */
	public void setIconSelector(FileChooserIconSelector iconSelector) {
		this.iconSelector = iconSelector;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setEmptyText(getString(R.string.empty_directory));
		setListAdapter(this.adapter);
		setListShown(false);

		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		FileListAdapter adapter = (FileListAdapter) l.getAdapter();
		if (adapter != null) {
			this.path = adapter.getItem(position);
			Activity activity = getActivity();
			if (activity instanceof FileChooserActivity) {
				((FileChooserActivity)getActivity()).onSelectedFile(this.path);				
			}

		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Loader<List<File>> onCreateLoader(int id, Bundle args) {
		return new AsyncFileLoader(getActivity(), this.path, this.fileFilter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onLoadFinished(Loader<List<File>> loader, List<File> data) {
		this.adapter.set(data);
		if (isResumed()) {
			setListShown(true);
		}
		else {
			setListShownNoAnimation(true);
		}
	}

	@Override
	public void onLoaderReset(Loader<List<File>> loader) {
		this.adapter.clear();
	}
}