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

import org.arakhne.afc.ui.android.R;
import org.arakhne.afc.vmutil.FileSystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * File chooser embedded inside an activity fragment. 
 * 
 * @author $Author: galland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class FileChooserActivity extends FragmentActivity implements OnBackStackChangedListener {

	/** Name of the extra data that is containing the options of the file chooser.
	 */
	static final String ACTIVITY_OPTIONS = "fileChooserActivityOptions"; //$NON-NLS-1$

	/** Name of the attribute that permits to save the path in the activity.
	 */
	public static final String SAVED_PATH_NAME = "path"; //$NON-NLS-1$

	/** Name of the attribute that permits to save the icon selector in the activity.
	 */
	public static final String SAVED_ICON_SELECTOR = "iconSelector"; //$NON-NLS-1$

	/** Name of the attribute that permits to save the file filter in the activity.
	 */
	public static final String SAVED_FILE_FILTER = "fileFilter"; //$NON-NLS-1$

	/** Name of the attribute that permits to indicates if the chooser is for opening a file.
	 */
	public static final String SAVED_IS_OPEN = "isOpen"; //$NON-NLS-1$

	/** Name of the preference that permits to store the path.
	 */
	private static final String PREFERENCE_FILE_PATH = "lastSelectedPath"; //$NON-NLS-1$

	private static File getFileParameter(String name, File defaultValue, Bundle... bundles) {
		for(Bundle b : bundles) {
			if (b!=null) {
				String absPath = b.getString(name);
				if (absPath!=null) return new File(absPath);
			}
		}
		return defaultValue;
	}

	private static FileFilter getFileFilterParameter(String name, FileFilter defaultValue, Bundle... bundles) {
		for(Bundle b : bundles) {
			if (b!=null) {
				String classname = b.getString(name);
				if (classname!=null) {
					try {
						Class<?> type = Class.forName(classname);
						if (FileFilter.class.isAssignableFrom(type)) {
							return (FileFilter)type.newInstance();
						}
					}
					catch(Throwable e) {
						Log.d("FILE_CHOOSER", e.getLocalizedMessage(), e); //$NON-NLS-1$
					}
				}
			}
		}
		return defaultValue;
	}

	private static FileChooserIconSelector getIconSelectorParameter(String name, FileChooserIconSelector defaultValue, Bundle... bundles) {
		for(Bundle b : bundles) {
			if (b!=null) {
				String classname = b.getString(name);
				if (classname!=null) {
					try {
						Class<?> type = Class.forName(classname);
						if (FileChooserIconSelector.class.isAssignableFrom(type)) {
							return (FileChooserIconSelector)type.newInstance();
						}
					}
					catch(Throwable e) {
						Log.d("FILE_CHOOSER", e.getLocalizedMessage(), e); //$NON-NLS-1$
					}
				}
			}
		}
		return defaultValue;
	}

	/** Listener on the external storage state.
	 */
	private BroadcastReceiver storageListener = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			onValidatedFile(null);
		}
	};

	/** Current selected file.
	 */
	private File path = null;

	/** File filter.
	 */
	private FileFilter fileFilter = null;

	/** Icon selector.
	 */
	private FileChooserIconSelector iconSelector = null;

	/** Manager of fragments.
	 */
	private FragmentManager fragmentManager = null;

	/** Indicates if the file chooser is for opening (<code>true</code>)
	 * or saving (<code>false</code>) a file.
	 */
	private boolean isOpen = true;
	
	/** Reference to the save menu item.
	 */
	private MenuItem saveItem = null;

	/** Reference to the up-directory menu item.
	 */
	private MenuItem upDirectoryItem = null;
	
	/** Options given to the activity.
	 */
	private Bundle activityOptions = null;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		this.activityOptions = intent.getBundleExtra(ACTIVITY_OPTIONS);

		this.path = getFileParameter(
				SAVED_PATH_NAME,
				null, 
				this.activityOptions, savedInstanceState);
		
		if (this.path==null) {
			SharedPreferences preferences = getPreferences(DEFAULT_KEYS_SEARCH_LOCAL);
			String filePath = preferences.getString(PREFERENCE_FILE_PATH, null);
			if (filePath!=null && !filePath.isEmpty()) {
				this.path = new File(filePath);
				if (!this.path.isDirectory()) {
					this.path = this.path.getParentFile();
				}
			}
			if (this.path==null) {
				this.path = Environment.getExternalStorageDirectory();
			}
		}
		
		this.fileFilter = getFileFilterParameter(
				SAVED_FILE_FILTER,
				null,
				this.activityOptions, savedInstanceState);

		this.iconSelector = getIconSelectorParameter(
				SAVED_ICON_SELECTOR,
				null,
				this.activityOptions, savedInstanceState);

		if (this.activityOptions!=null) {
			this.isOpen = this.activityOptions.getBoolean(SAVED_IS_OPEN,
					savedInstanceState!=null ?
					savedInstanceState.getBoolean(SAVED_IS_OPEN, true) :
					true);
		}

		String basename = null;
		if (!this.isOpen && !this.path.isDirectory()) {
			basename = this.path.getName();
		}
		
		while (this.path!=null && !this.path.isDirectory()) {
			this.path = this.path.getParentFile();
		}

		if (this.path==null) {
			this.path = Environment.getExternalStorageDirectory();
		}

		if (this.isOpen) {
			setContentView(R.layout.filechooser_open);
		}
		else {
			setContentView(R.layout.filechooser_save);
			EditText editor = (EditText)findViewById(R.id.fileChooserFilenameField);
			if (basename!=null) editor.setText(basename);
			editor.addTextChangedListener(new Listener());
		}

		this.fragmentManager = getSupportFragmentManager();
		this.fragmentManager.addOnBackStackChangedListener(this);

		setTitle(this.path.getAbsolutePath());

		// Start to listen on directory changes.
		FileListFragment explorerFragment = FileListFragment.newInstance(
				this.path, this.fileFilter, this.iconSelector);
		FragmentTransaction transaction = this.fragmentManager.beginTransaction();
		transaction.add(
				R.id.fileChooserExplorerFragment,
				explorerFragment).commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (this.isOpen) {
			getMenuInflater().inflate(R.menu.filechooser_open_menu, menu);
		}
		else {
			getMenuInflater().inflate(R.menu.filechooser_save_menu, menu);
			this.saveItem = menu.findItem(R.id.fileChooserSaveMenu);
		}
		this.upDirectoryItem = menu.findItem(R.id.fileChooserParentDirectoryMenu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId()==R.id.fileChooserParentDirectoryMenu) {
			onSelectedFile(this.path.getParentFile());
			return true;
		}
		if (!this.isOpen && item.getItemId()==R.id.fileChooserSaveMenu) {
			EditText editor = (EditText)findViewById(R.id.fileChooserFilenameField);
			String basename = editor.getText().toString();
			basename = FileSystem.basename(basename);
			onValidatedFile(new File(this.path, basename));
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		updateUpDirectoryAction();
		if (!this.isOpen) {
			updateSaveAction();
		}
		return true;
	}
	
	/** Update the state of the up-directory action.
	 */
	protected void updateUpDirectoryAction() {
		if (this.upDirectoryItem!=null) {
			this.upDirectoryItem.setEnabled(this.path.getParentFile()!=null);
		}
	}

	/** Update the state of the save action.
	 */
	protected void updateSaveAction() {
		if (this.saveItem!=null) {
			EditText editor = (EditText)findViewById(R.id.fileChooserFilenameField);
			String text = editor.getText().toString();
			this.saveItem.setEnabled(this.path.isDirectory() && !text.isEmpty());
		}
	}

	/** Set the file filter to use in the chooser.
	 * 
	 * @param fileFilter
	 */
	public void setFileFilter(FileFilter fileFilter) {
		this.fileFilter = fileFilter;
	}

	/** Replies the file filter to use in the chooser.
	 * 
	 * @return the file filter.
	 */
	public FileFilter getFileFilter() {
		return this.fileFilter;
	}

	/** Set the selector of icon
	 * 
	 * @param iconSelector
	 */
	public void setIconSelector(FileChooserIconSelector iconSelector) {
		this.iconSelector = iconSelector;
	}

	/** Replies the selector of icon.
	 * 
	 * @return the icon selector.
	 */
	public FileChooserIconSelector getIconSelector() {
		return this.iconSelector;
	}

	/** Invoked when the SD card state has changed.
	 */
	@Override
	public void onBackStackChanged() {
		this.path = Environment.getExternalStorageDirectory();

		int count = this.fragmentManager.getBackStackEntryCount();
		if (count > 0) {
			BackStackEntry fragment = this.fragmentManager.getBackStackEntryAt(count - 1);
			String path = fragment.getName();
			if (path!=null)
				this.path = new File(path);
		}

		setTitle(this.path.getAbsolutePath());
		invalidateOptionsMenu();
	}

	/** {@inheritDoc}
	 */
	@Override
	protected void onPause() {
		super.onPause();
		unregisterStorageListener();
	}

	/** {@inheritDoc}
	 */
	@Override
	protected void onResume() {
		super.onResume();
		registerStorageListener();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (this.path!=null) {
			outState.putString(SAVED_PATH_NAME, this.path.getAbsolutePath());
		}
		else {
			outState.remove(SAVED_PATH_NAME);
		}
		if (this.fileFilter!=null) {
			outState.putString(SAVED_FILE_FILTER, this.fileFilter.getClass().getName());
		}
		else {
			outState.remove(SAVED_FILE_FILTER);
		}
		if (this.iconSelector!=null) {
			outState.putString(SAVED_ICON_SELECTOR, this.iconSelector.getClass().getName());
		}
		else {
			outState.remove(SAVED_ICON_SELECTOR);
		}
		outState.putBoolean(SAVED_IS_OPEN, this.isOpen);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		this.isOpen = savedInstanceState.getBoolean(SAVED_IS_OPEN);
		String fileFilterClassname = savedInstanceState.getString(SAVED_FILE_FILTER);
		if (fileFilterClassname!=null) {
			try {
				Class<?> type = Class.forName(fileFilterClassname);
				if (FileFilter.class.isAssignableFrom(type)) {
					this.fileFilter = (FileFilter)type.newInstance();
				}
			}
			catch(Throwable _) {
				//
			}
		}
		String iconSelectorClassname = savedInstanceState.getString(SAVED_ICON_SELECTOR);
		if (iconSelectorClassname!=null) {
			try {
				Class<?> type = Class.forName(iconSelectorClassname);
				if (FileChooserIconSelector.class.isAssignableFrom(type)) {
					this.iconSelector = (FileChooserIconSelector)type.newInstance();
				}
			}
			catch(Throwable _) {
				//
			}
		}
		String path = savedInstanceState.getString(SAVED_PATH_NAME);
		if (path!=null) {
			this.path = new File(path);
		}
	}

	/**
	 * Invoked when the activity is finished with a selected file.
	 * 
	 * @param file is the selected file.
	 */
	protected void onValidatedFile(File file) {
		// Force to close the soft keyboard
		if (!this.isOpen) {
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
		}
		File validatedFile = file;
		if (validatedFile!=null) {
			if (!this.isOpen && this.fileFilter instanceof org.arakhne.afc.io.filefilter.FileFilter) {
				if (!this.fileFilter.accept(validatedFile)) {
					String ext = ((org.arakhne.afc.io.filefilter.FileFilter)this.fileFilter).getExtensions()[0];
					validatedFile = FileSystem.addExtension(validatedFile, ext);
				}
			}

			if (this.path!=null) {
				File preferencePath = this.path;
				while(preferencePath!=null && !preferencePath.isDirectory())
					preferencePath = preferencePath.getParentFile();
				if (preferencePath!=null) {
					SharedPreferences preferences = getPreferences(DEFAULT_KEYS_SEARCH_LOCAL);
					Editor preferenceEditor = preferences.edit();
					preferenceEditor.putString(PREFERENCE_FILE_PATH, preferencePath.getAbsolutePath());
					preferenceEditor.commit();
					preferenceEditor.apply();
				}
			}
			
			Intent data = new Intent();
			data.setData(Uri.fromFile(validatedFile));
			
			if (this.activityOptions!=null) {
				data.putExtras(this.activityOptions);
			}
			
			setResult(RESULT_OK, data);
			finish();
		}
		else {
			setResult(RESULT_CANCELED);	
			finish();
		}
	}


	/**
	 * Called when the user selects a File.
	 * 
	 * @param file is the selected file.
	 */
	protected void onSelectedFile(File file) {
		if (file!=null) {
			this.path = file;

			if (file.isDirectory()) {
				FileListFragment explorerFragment = FileListFragment.newInstance(this.path, this.fileFilter, this.iconSelector);
				FragmentTransaction transaction = this.fragmentManager.beginTransaction();
				transaction.replace(
						R.id.fileChooserExplorerFragment,
						explorerFragment)
						.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
						.addToBackStack(this.path.getAbsolutePath()).commit();
			}
			else {
				onValidatedFile(file);	
			}
		}
	}

	/**
	 * Register the external storage BroadcastReceiver.
	 */
	private void registerStorageListener() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_MEDIA_REMOVED);
		registerReceiver(this.storageListener, filter);
	}

	/**
	 * Unregister the external storage BroadcastReceiver.
	 */
	private void unregisterStorageListener() {
		unregisterReceiver(this.storageListener);
	}
	
	/**
	 * @author $Author: galland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class Listener implements TextWatcher {
		
		/**
		 */
		public Listener() {
			//
		}
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			//
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			//
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			updateSaveAction();
		}
		
	} // class Listener

}