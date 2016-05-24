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
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
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