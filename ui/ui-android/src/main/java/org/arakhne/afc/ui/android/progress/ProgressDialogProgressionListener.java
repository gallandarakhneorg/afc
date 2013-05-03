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
package org.arakhne.afc.ui.android.progress;

import org.arakhne.afc.progress.ProgressionEvent;
import org.arakhne.afc.progress.ProgressionListener;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * Task progression listener that is linked to a ProgressDialog.
 * 
 * @author $Author: galland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
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
	 * @author $Author: galland$
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