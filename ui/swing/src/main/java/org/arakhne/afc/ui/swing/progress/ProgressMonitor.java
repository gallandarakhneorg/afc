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

package org.arakhne.afc.ui.swing.progress;

import java.awt.Component;
import java.lang.ref.WeakReference;

import javax.swing.JOptionPane;

import org.arakhne.afc.progress.DefaultProgression;
import org.arakhne.afc.progress.Progression;
import org.arakhne.afc.progress.ProgressionEvent;
import org.arakhne.afc.progress.ProgressionListener;

/**
 * A ProgressMonitor ables to react to {@link ProgressionEvent}.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public class ProgressMonitor extends javax.swing.ProgressMonitor implements ProgressionListener {

	/** Create a {@link Progression} model and link it to a
	 * Swing {@link ProgressMonitor}.
	 * 
	 * @param parentComponent the parent component for the dialog box
     * @param message a descriptive message that will be shown
     *        to the user to indicate what operation is being monitored.
     *        This does not change as the operation progresses.
     *        See the message parameters to methods in
     *        {@link JOptionPane#message}
     *        for the range of values.
     * @param note a short note describing the state of the
     *        operation.  As the operation progresses, you can call
     *        setNote to change the note displayed.  This is used,
     *        for example, in operations that iterate through a
     *        list of files to show the name of the file being processes.
     *        If note is initially null, there will be no note line
     *        in the dialog box and setNote will be ineffective
     * @return the progression model.
	 */
	public static Progression createProgression(Component parentComponent,
            Object message,
            String note) {
		Progression p = new DefaultProgression();
		p.addProgressionListener(new ProgressMonitor(
				p,
				parentComponent, message, note, p.getMinimum(), p.getMaximum()));
		return p;
	}
	
	private final WeakReference<Progression> progressionModel;
	
	/**
	 * @param parentComponent the parent component for the dialog box
     * @param message a descriptive message that will be shown
     *        to the user to indicate what operation is being monitored.
     *        This does not change as the operation progresses.
     *        See the message parameters to methods in
     *        {@link JOptionPane#message}
     *        for the range of values.
     * @param note a short note describing the state of the
     *        operation.  As the operation progresses, you can call
     *        setNote to change the note displayed.  This is used,
     *        for example, in operations that iterate through a
     *        list of files to show the name of the file being processes.
     *        If note is initially null, there will be no note line
     *        in the dialog box and setNote will be ineffective
     * @param min the lower bound of the range
     * @param max the upper bound of the range
     * @see #createProgression(Component, Object, String)
	 */
	public ProgressMonitor(Component parentComponent,
            Object message,
            String note,
            int min,
            int max) {
		this(null, parentComponent, message, note, min, max);
	}

	private ProgressMonitor(Progression progressionModel,
			Component parentComponent,
            Object message,
            String note,
            int min,
            int max) {
		super(parentComponent, message, note, min, max);
		this.progressionModel = new WeakReference<>(progressionModel);
	}
	
	@Override
	public void onProgressionValueChanged(ProgressionEvent event) {
		if (event.isFinished()) {
			Progression progression = this.progressionModel.get();
			this.progressionModel.clear();
			if (progression!=null) {
				progression.removeProgressionListener(this);
			}
			close();
		}
		else {
			setMinimum(event.getMinimum());
			setMaximum(event.getMaximum());
			setProgress(event.getValue());
			setNote(event.getComment());
		}
	}

	@Override
	public void onProgressionStateChanged(ProgressionEvent event) {
		if (event.isFinished()) {
			setNote(event.getComment());
		}
	}	
	
}
