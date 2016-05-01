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

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;


/**
 * Utilities about file choosers.  
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public class FileChooser {

	/** Show the file chooser for opening a file.
	 * <p>
	 * The keys supported by the file chooser are:<ul>
	 * <li><code>path</code>: the directory path to open at start-up.</li>
	 * <li><code>fileFilter</code>: the classname of the file filter to use.</li>
	 * </ul>
	 * 
	 * @param context is the execution context.
	 * @param activityResultRequestCode is the code that may be used to retreive the result of the activity.
	 * @param chooserTitle is the identifier for the chooser's title.
	 * @param mimeType is the MIME type of the files to select.
	 * @param options are the options to pass to the activity.
	 */
	public static void showOpenChooser(Activity context, 
			int activityResultRequestCode, 
			int chooserTitle,
			String mimeType,
			Bundle options) {
		// Implicitly allow the user to select a particular kind of data
		Intent target = new Intent(Intent.ACTION_GET_CONTENT); 
		// The MIME data type filter
		target.setType(mimeType);
		// Only return URIs that can be opened with ContentResolver
		target.addCategory(Intent.CATEGORY_OPENABLE);
		// Create the chooser Intent
		Intent intent = Intent.createChooser(
				target, context.getString(chooserTitle));
		// Set the extra data to pass to the activity.
		Bundle opts;
		if (options!=null) {
			opts = new Bundle(options);
		}
		else {
			opts = new Bundle();
		}
		opts.putBoolean(FileChooserActivity.SAVED_IS_OPEN, true);
		intent.putExtra(FileChooserActivity.ACTIVITY_OPTIONS, opts);
		Parcelable innerIntent = intent.getParcelableExtra(Intent.EXTRA_INTENT);
		if (innerIntent instanceof Intent)
			((Intent)innerIntent).putExtra(FileChooserActivity.ACTIVITY_OPTIONS, opts);
		try {
			context.startActivityForResult(intent, activityResultRequestCode);
		}
		catch (ActivityNotFoundException e) {
			Log.e("FILE_CHOOSER", e.getLocalizedMessage(), e); //$NON-NLS-1$
		}	
	}

	/** Show the file chooser for opening a file.
	 * <p>
	 * The keys supported by the file chooser are:<ul>
	 * <li><code>path</code>: the directory path to open at start-up.</li>
	 * <li><code>fileFilter</code>: the classname of the file filter to use.</li>
	 * </ul>
	 * <p>
	 * Mime type is "file/*".
	 * 
	 * @param context is the execution context.
	 * @param activityResultRequestCode is the code that may be used to retreive the result of the activity.
	 * @param chooserTitle is the identifier for the chooser's title.
	 * @param options are the options to pass to the activity.
	 */
	public static void showOpenChooser(Activity context, 
			int activityResultRequestCode, 
			int chooserTitle,
			Bundle options) {
		showOpenChooser(context, activityResultRequestCode, chooserTitle, "file/*", options); //$NON-NLS-1$
	}

	/** Show the file chooser for opening a file.
	 * <p>
	 * Mime type is "file/*".
	 * 
	 * @param context is the execution context.
	 * @param activityResultRequestCode is the code that may be used to retreive the result of the activity.
	 * @param chooserTitle is the identifier for the chooser's title.
	 * @param directory is the directory to explore.
	 * @param fileFilter is the file filter to use.
	 * @param iconSelector is the selector of icon to use; or <code>null</code> for none.
	 */
	public static void showOpenChooser(Activity context, 
			int activityResultRequestCode, 
			int chooserTitle,
			File directory,
			Class<? extends FileFilter> fileFilter,
			Class<? extends FileChooserIconSelector> iconSelector) {
		showOpenChooser(context, activityResultRequestCode, chooserTitle,
				createOptions(directory, fileFilter, iconSelector));
	}
	
	/** Create options that may be passed to a chooser.
	 * 
	 * @param directory is the directory to explore.
	 * @param fileFilter is the file filter to use.
	 * @param iconSelector is the selector of icon to use; or <code>null</code> for none.
	 * @return the options.
	 */
	public static Bundle createOptions(
			File directory,
			Class<? extends FileFilter> fileFilter,
			Class<? extends FileChooserIconSelector> iconSelector) {
		Bundle bundle = new Bundle();
		if (directory!=null) {
			bundle.putString(FileChooserActivity.SAVED_PATH_NAME, directory.getAbsolutePath());
		}
		if (fileFilter!=null) {
			bundle.putString(FileChooserActivity.SAVED_FILE_FILTER, fileFilter.getName());
		}
		if (iconSelector!=null) {
			bundle.putString(FileChooserActivity.SAVED_ICON_SELECTOR, iconSelector.getName());
		}
		return bundle;
	}
	
	/** Show the file chooser for opening a file.
	 * <p>
	 * Mime type is "file/*".
	 * 
	 * @param context is the execution context.
	 * @param activityResultRequestCode is the code that may be used to retreive the result of the activity.
	 * @param chooserTitle is the identifier for the chooser's title.
	 */
	public static void showOpenChooser(Activity context, 
			int activityResultRequestCode, 
			int chooserTitle) {
		showOpenChooser(context, activityResultRequestCode, chooserTitle, null);
	}

	/** Show the file chooser for saving a file.
	 * <p>
	 * The keys supported by the file chooser are:<ul>
	 * <li><code>path</code>: the file path to save at start-up.</li>
	 * <li><code>fileFilter</code>: the classname of the file filter to use.</li>
	 * </ul>
	 * 
	 * @param context is the execution context.
	 * @param activityResultRequestCode is the code that may be used to retreive the result of the activity.
	 * @param chooserTitle is the identifier for the chooser's title.
	 * @param mimeType is the mime type of the files to select.
	 * @param options are the options to pass to the activity.
	 */
	public static void showSaveChooser(Activity context, 
			int activityResultRequestCode, 
			int chooserTitle,
			String mimeType,
			Bundle options) {
		// Implicitly allow the user to select a particular kind of data
		Intent target = new Intent(Intent.ACTION_GET_CONTENT); 
		// The MIME data type filter
		target.setType(mimeType);
		// Only return URIs that can be opened with ContentResolver
		target.addCategory(Intent.CATEGORY_OPENABLE);
		// Create the chooser Intent
		Intent intent = Intent.createChooser(
				target, context.getString(chooserTitle));
		// Set the extra data to pass to the activity.
		Bundle opts;
		if (options!=null) {
			opts = new Bundle(options);
		}
		else {
			opts = new Bundle();
		}
		opts.putBoolean(FileChooserActivity.SAVED_IS_OPEN, false);
		intent.putExtra(FileChooserActivity.ACTIVITY_OPTIONS, opts);
		Parcelable innerIntent = intent.getParcelableExtra(Intent.EXTRA_INTENT);
		if (innerIntent instanceof Intent)
			((Intent)innerIntent).putExtra(FileChooserActivity.ACTIVITY_OPTIONS, opts);
		try {
			context.startActivityForResult(intent, activityResultRequestCode);
		}
		catch (ActivityNotFoundException e) {
			Log.e("FILE_CHOOSER", e.getLocalizedMessage(), e); //$NON-NLS-1$
		}	
	}

	/** Show the file chooser for saving a file.
	 * <p>
	 * The keys supported by the file chooser are:<ul>
	 * <li><code>path</code>: the file path to save at start-up.</li>
	 * <li><code>fileFilter</code>: the classname of the file filter to use.</li>
	 * </ul>
	 * <p>
	 * Mime type is "file/*".
	 * 
	 * @param context is the execution context.
	 * @param activityResultRequestCode is the code that may be used to retreive the result of the activity.
	 * @param chooserTitle is the identifier for the chooser's title.
	 * @param options are the options to pass to the activity.
	 */
	public static void showSaveChooser(Activity context, 
			int activityResultRequestCode, 
			int chooserTitle,
			Bundle options) {
		showSaveChooser(context, activityResultRequestCode, chooserTitle, "file/*", options); //$NON-NLS-1$
	}

	/** Show the file chooser for saving a file.
	 * <p>
	 * Mime type is "file/*".
	 * 
	 * @param context is the execution context.
	 * @param activityResultRequestCode is the code that may be used to retreive the result of the activity.
	 * @param chooserTitle is the identifier for the chooser's title.
	 * @param file is the filename to use for the saving file.
	 * @param fileFilter is the file filter to use.
	 * @param iconSelector is the selector of icon to use; or <code>null</code> for none.
	 */
	public static void showSaveChooser(Activity context, 
			int activityResultRequestCode, 
			int chooserTitle,
			File file,
			Class<? extends FileFilter> fileFilter,
			Class<? extends FileChooserIconSelector> iconSelector) {
		showSaveChooser(context, activityResultRequestCode, chooserTitle,
				createOptions(file, fileFilter, iconSelector));
	}

	/** Show the file chooser for saving a file.
	 * <p>
	 * Mime type is "file/*".
	 * 
	 * @param context is the execution context.
	 * @param activityResultRequestCode is the code that may be used to retreive the result of the activity.
	 * @param chooserTitle is the identifier for the chooser's title.
	 */
	public static void showSaveChooser(Activity context, 
			int activityResultRequestCode, 
			int chooserTitle) {
		showSaveChooser(context, activityResultRequestCode, chooserTitle, null);
	}

}