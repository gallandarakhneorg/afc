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

package org.arakhne.afc.ui.android.progress;

import org.arakhne.afc.progress.ProgressionEvent;
import org.arakhne.afc.progress.ProgressionListener;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * Task progression listener that is linked to a ProgressDialog.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public class ProgressDialogProgressionListener implements ProgressionListener {

	private final Activity context;
	private final ProgressDialog dialog;
	private final String initialMessage;
	private String currentMessage;

	/**
	 * @param context
	 * @param dialog
	 * @param initialMessage
	 */
	public ProgressDialogProgressionListener(Activity context, ProgressDialog dialog, String initialMessage) {
		this.context = context;
		this.dialog = dialog;
		this.currentMessage = this.initialMessage = initialMessage;
	}

	@Override
	public void onProgressionValueChanged(ProgressionEvent event) {
		int v;
		v = event.getMaximum() - event.getMinimum();
		if (this.dialog.getMax()!=v) this.dialog.setMax(v);
		v = event.getValue() - event.getMinimum();
		if (this.dialog.getProgress()!=v) this.dialog.setProgress(v);
		updateComment(event.getComment());
	}

	@Override
	public void onProgressionStateChanged(ProgressionEvent event) {
		this.dialog.setIndeterminate(event.isIndeterminate());
		updateComment(event.getComment());
	}
	
	private void updateComment(String newComment) {
		String rComment = newComment;
		if (rComment==null) rComment = this.initialMessage;
		if (!rComment.equals(this.currentMessage)) {
			this.currentMessage = rComment;
			this.context.runOnUiThread(new CommentUpdater(this.dialog, rComment));
		}
	}
	
	/**
	 * Task progression listener that is linked to a ProgressDialog.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class CommentUpdater implements Runnable {

		private final String comment;
		private final ProgressDialog dialog;

		/**
		 * @param dialog
		 * @param comment
		 */
		public CommentUpdater(ProgressDialog dialog, String comment) {
			this.dialog = dialog;
			this.comment = comment;
		}

		@Override
		public void run() {
			this.dialog.setMessage(this.comment);
		}
		
	} // class CommentUpdater
	
}