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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.arakhne.afc.util.ListUtil;

import android.content.Context;
import android.os.FileObserver;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Asynchronous loader of a list of files.
 * This loader is watching the file system content
 * to update the list of the files when this list
 * was changed by the action of an other application.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
class AsyncFileLoader extends AsyncTaskLoader<List<File>> {

	private final File path;
	private final FileFilter filter;
	
	private FileObserver fileObserver;

	private List<File> discoveredFiles = null;

	/**
	 * @param context is the execution context of the task.
	 * @param path is the path of the directory to explore.
	 * @param fileFilter is the filter to use.
	 */
	public AsyncFileLoader(Context context, File path, FileFilter fileFilter) {
		super(context);
		this.path = path;
		this.filter = fileFilter;
	}
	
	/** Replies if the given file is hidden in the file chooser.
	 * 
	 * @param file
	 * @return <code>true</code> if hidden; <code>false</code> otherwise.
	 */
	@SuppressWarnings("static-method")
	protected boolean isHiddenFile(File file) {
		return file.isHidden() || file.getName().equalsIgnoreCase("lost.dir"); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<File> loadInBackground() {
		File[] array;
		if (this.filter!=null) {
			array = this.path.listFiles(this.filter);
		}
		else {
			array = this.path.listFiles();
		}
		List<File> list = new ArrayList<>(array.length);
		// Dichotomic insertion
		Comparator<File> comparator = new Comparator<File>() {
			@Override
			public int compare(File lhs, File rhs) {
				String ln = lhs.getName();
				String rn = rhs.getName();
				return ln.compareToIgnoreCase(rn);
			}
		};
		for(File file : array) {
			if (!isHiddenFile(file)) {
				ListUtil.add(list, comparator, file, false, true);
			}
		}
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onStartLoading() {
		// A list is already available. Publish it.
		if (this.discoveredFiles != null) deliverResult(this.discoveredFiles);

		if (this.fileObserver == null) {
			this.fileObserver = new FileObserver(this.path.getAbsolutePath(), 
					FileObserver.CREATE
					| FileObserver.DELETE | FileObserver.DELETE_SELF
					| FileObserver.MOVED_FROM | FileObserver.MOVED_TO
					| FileObserver.MODIFY | FileObserver.MOVE_SELF) {
				@Override
				public void onEvent(int event, String path) {
					onContentChanged();	
				}
			};
		}
		this.fileObserver.startWatching();

		if (takeContentChanged() || this.discoveredFiles==null)
			forceLoad();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void deliverResult(List<File> data) {
		if (isReset()) {
			unwatch();
			return;
		}

		List<File> old = this.discoveredFiles;
		this.discoveredFiles = data;

		if (isStarted())
			super.deliverResult(this.discoveredFiles);

		if (old!=null && old!=data)
			unwatch();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onReset() {
		onStopLoading();

		if (this.discoveredFiles!=null) {
			unwatch();
			this.discoveredFiles = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCanceled(List<File> data) {
		super.onCanceled(data);
		unwatch();
	}

	/** Unwatch the state for the given files.
	 */
	protected void unwatch() {
		if (this.fileObserver != null) {
			this.fileObserver.stopWatching();
			this.fileObserver = null;
		}
	}
}