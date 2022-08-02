/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

	/** Construct a monitor.
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
		if (model == null) {
			this.model = new DefaultProgression();
		} else {
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
	@SuppressWarnings("checkstyle:regexp")
	public void onProgressionValueChanged(ProgressionEvent event) {
		if (!event.isIndeterminate() && event.getValue() != this.previousValue) {
			this.previousValue = event.getValue();
			final String message = buildMessage(
					event.getProgressionFactor(),
					event.getComment(),
					event.isRoot(),
					event.isFinished(),
					this.numberFormat);
			if (this.logger == null) {
				System.out.println(message);
			} else {
				this.logger.info(message);
			}
		}
	}

	/** Build the logging message from the given data.
	 * This function is defined for enabling overriding in sub classes.
	 *
	 * @param progress - progression indicator between 0 and 1.
	 * @param comment - associated comment.
	 * @param isRoot - indicates if the progression model is a root model.
	 * @param isFinished - indicates if the task is finished.
	 * @param numberFormat - instance of the number formatter.
	 * @return the message.
	 */
	@SuppressWarnings("static-method")
	protected String buildMessage(double progress, String comment, boolean isRoot, boolean isFinished,
			NumberFormat numberFormat) {
		final StringBuilder txt = new StringBuilder();
		txt.append('[');
		txt.append(numberFormat.format(progress));
		txt.append("] "); //$NON-NLS-1$
		if (comment != null) {
			txt.append(comment);
		}
		return txt.toString();
	}

	/** Progression listener that is weakly referencing another listener.
	 *
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class WeakListener implements ProgressionListener {

		private final WeakReference<ProgressionListener> listener;

		private final WeakReference<Progression> model;

		/** Constuct a listener.
		 *
		 * @param listener the original listener.
		 * @param model the progression model.
		 */
		WeakListener(ProgressionListener listener, Progression model) {
			this.listener = new WeakReference<>(listener);
			this.model = new WeakReference<>(model);
		}

		@Override
		public void onProgressionValueChanged(ProgressionEvent event) {
			final ProgressionListener list = this.listener.get();
			if (list != null) {
				list.onProgressionValueChanged(event);
			} else {
				removeListener();
			}
		}

		@Override
		public void onProgressionStateChanged(ProgressionEvent event) {
			final ProgressionListener list = this.listener.get();
			if (list != null) {
				list.onProgressionStateChanged(event);
			} else {
				removeListener();
			}
		}

		private void removeListener() {
			final Progression prog = this.model.get();
			if (prog != null) {
				prog.removeProgressionListener(this);
			}
		}

	}

}
