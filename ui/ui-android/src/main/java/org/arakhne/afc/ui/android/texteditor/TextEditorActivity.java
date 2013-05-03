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
package org.arakhne.afc.ui.android.texteditor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

import org.arakhne.afc.ui.android.R;
import org.arakhne.vmutil.FileSystem;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

/**
 * Text editor embedded inside an activity fragment. 
 * 
 * @author $Author: galland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class TextEditorActivity extends Activity {

	private EditText textView = null;
	private File file = null;
	private boolean changedSinceOriginal = false;
	private boolean changedSinceLastSave = false;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.texteditor);
		
		this.file = null;
		this.changedSinceOriginal = false;
		this.changedSinceLastSave = false;
		this.textView = (EditText)findViewById(R.id.textEditor);
		
		Intent intent = getIntent();
		
		this.textView.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				//
			}
			@SuppressWarnings("synthetic-access")
			@Override
			public void afterTextChanged(Editable s) {
				if (!TextEditorActivity.this.changedSinceLastSave) {
					TextEditorActivity.this.changedSinceLastSave = true;
					TextEditorActivity.this.changedSinceOriginal = true;
					TextEditorActivity.this.invalidateOptionsMenu();
				}
			}
		});
		
		Uri uri = intent.getData();
		if (uri!=null) {
			try {
				URI jURI = new URI(uri.toString());
				URL jURL = jURI.toURL();
				
				try {
					this.file = FileSystem.convertURLToFile(jURL);
				}
				catch(Throwable _) {
					//
				}
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(jURL.openStream()));
				try {
					StringBuilder text = new StringBuilder();
					char[] buffer = new char[2048];
					int n = reader.read(buffer, 0, 2048);
					while (n>0) {
						text.append(buffer, 0, n);
						n = reader.read(buffer, 0, 2048);
					}
					this.textView.setText(text);
				}
				finally {
					reader.close();
				}
			}
			catch(Throwable e) {
				Log.w(getClass().getName(), e.getLocalizedMessage(), e);
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.texteditor_menu, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		MenuItem item;
		item = menu.findItem(R.id.textEditorSaveMenu);
		item.setEnabled(this.file!=null && this.changedSinceLastSave);
		item = menu.findItem(R.id.textEditorUploadMenu);
		item.setEnabled(this.file!=null && this.changedSinceOriginal);
		return true;
	}

	private void save() {
		if (this.file!=null && this.changedSinceLastSave) {
			try {
				FileWriter fw = new FileWriter(this.file);
				try {
					fw.write(this.textView.getText().toString());
					fw.flush();
				}
				finally {
					fw.close();
				}
			}
			catch(Throwable e) {
				Log.w(getClass().getName(), e.getLocalizedMessage(), e);
			}
			this.changedSinceLastSave = false;
			invalidateOptionsMenu();
		}
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int id = item.getItemId();
		if (id==R.id.textEditorSaveMenu) {
			save();
			return true;
		}
		if (id==R.id.textEditorUploadMenu) {
			save();
			Intent data = new Intent();
			Uri uri = Uri.fromFile(this.file);
			data.setData(uri);
			setResult(RESULT_OK, data);
			finish();
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}
		
}