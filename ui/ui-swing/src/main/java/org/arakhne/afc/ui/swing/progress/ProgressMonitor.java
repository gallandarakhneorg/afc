/* 
 * $Id$
 * 
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
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
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
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
		this.progressionModel = new WeakReference<Progression>(progressionModel);
	}
	
	@Override
	public void onProgressionValueChanged(ProgressionEvent event) {
		if (event.isTaskFinished()) {
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
		if (event.isTaskFinished()) {
			setNote(event.getComment());
		}
	}	
	
}
