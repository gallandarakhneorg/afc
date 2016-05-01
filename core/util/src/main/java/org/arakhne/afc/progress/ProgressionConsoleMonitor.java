/* 
 * $Id$
 * 
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (C) 2012 Stephane GALLAND.
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
package org.arakhne.afc.progress;

import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.util.logging.Logger;

/**
 * An object that permits to indicates the progression of
 * a task. This monitor display on the console the progression
 * of the task.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ProgressionConsoleMonitor implements ProgressionListener {

	private final NumberFormat numberFormat;
	private Progression model = new DefaultProgression();
	private Logger logger;
	private int previousValue;
	
	/**
	 */
	public ProgressionConsoleMonitor() {
		this.numberFormat = NumberFormat.getPercentInstance();
		this.numberFormat.setMaximumFractionDigits(0);
		this.logger = null;
		this.previousValue = this.model.getValue();
		this.model.addProgressionListener(new WeakListener(this, this.model));
	}
	
	/** Replies the task progression model.
	 * 
	 * @return the task progression model.
	 */
	public Progression getModel() {
		return this.model;
	}

	/** Change the task progression model.
	 * 
	 * @param model - the task progression model.
	 */
	public void setModel(Progression model) {
		this.model.removeProgressionListener(new WeakListener(this, this.model));
		if (model==null) {
			this.model = new DefaultProgression();
		}
		else {
			this.model = model;
		}
		this.previousValue = this.model.getValue();
		this.model.addProgressionListener(new WeakListener(this, this.model));
	}
	
	/** Replies the logger used by this monitor.
	 * 
	 * @return the logger, never <code>null</code>
	 */
	public Logger getLogger() {
		return this.logger;
	}
	
	/** Change the logger used by this monitor.
	 * If the given logger is <code>null</code>, then
	 * the {@link Logger#getAnonymousLogger() default anonymous logger}
	 * will be used.
	 * 
	 * @param logger - the new logger.
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	@Override
	public void onProgressionStateChanged(ProgressionEvent event) {
		//
	}

	@Override
	public void onProgressionValueChanged(ProgressionEvent event) {
		if (!event.isIndeterminate() && event.getValue()!=this.previousValue) {
			this.previousValue = event.getValue();
			String m = buildMessage(
					event.getProgressionFactor(),
					event.getComment(),
					event.isRoot(),
					event.isFinished(),
					this.numberFormat);
			if (this.logger==null) {
				System.out.println(m);
			}
			else {
				this.logger.info(m);
			}
		}
	}
	
	/** Build the logging message from the given data.
	 * This function is defined for enabling overriding in sub classes.
	 * 
	 * @param progress - progression indicator between 0 and 1 
	 * @param comment - associated comment.
	 * @param isRoot - indicates if the progression model is a root model.
	 * @param isFinished - indicates if the task is finished.
	 * @param numberFormat - instance of the number formatter.
	 * @return the message.
	 */
	@SuppressWarnings("static-method")
	protected String buildMessage(double progress, String comment, boolean isRoot, boolean isFinished, NumberFormat numberFormat) {
		StringBuilder txt = new StringBuilder();
		txt.append('[');
		txt.append(numberFormat.format(progress));
		txt.append("] "); //$NON-NLS-1$
		if (comment!=null) {
			txt.append(comment);
		}
		return txt.toString();
	}

	/**
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class WeakListener implements ProgressionListener {
		
		private final WeakReference<ProgressionListener> listener;
		private final WeakReference<Progression> model;
		
		/**
		 * @param listener
		 * @param model
		 */
		public WeakListener(ProgressionListener listener, Progression model) {
			this.listener = new WeakReference<>(listener);
			this.model = new WeakReference<>(model);
		}

		@Override
		public void onProgressionValueChanged(ProgressionEvent event) {
			ProgressionListener l = this.listener.get();
			if (l!=null) {
				l.onProgressionValueChanged(event);
			}
			else {
				removeListener();
			}
		}

		@Override
		public void onProgressionStateChanged(ProgressionEvent event) {
			ProgressionListener l = this.listener.get();
			if (l!=null) {
				l.onProgressionStateChanged(event);
			}
			else {
				removeListener();
			}
		}
		
		private void removeListener() {
			Progression p = this.model.get();
			if (p!=null) {
				p.removeProgressionListener(this);
			}
		}
		
	}
	
}
